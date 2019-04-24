package werkzeuge.graphwerkzeug.presentation;

import com.intellij.openapi.graph.GraphManager;
import com.intellij.openapi.graph.base.Edge;
import com.intellij.openapi.graph.base.Node;
import com.intellij.openapi.graph.layout.BufferedLayouter;
import com.intellij.openapi.graph.layout.LayoutGraph;
import com.intellij.openapi.graph.layout.Layouter;
import com.intellij.openapi.graph.layout.router.GroupNodeRouterStage;
import com.intellij.openapi.graph.layout.router.OrthogonalEdgeRouter;
import com.intellij.openapi.graph.layout.router.PartitionGridRouterStage;
import materials.ClassDependencyComparator;
import materials.ProgramEntity;
import materials.ProgramEntityRelationship;
import materials.TraceLinkProgramEntityAssociation;
import valueobjects.Language;
import werkzeuge.graphwerkzeug.ImpactAnalysisGraph;

import java.util.*;

/**
 * A custom Layouter for comparing ClassNodes that are associated to ClassNodes through a TraceabilityClassDependency
 */
public class TraceabilityCompLayouter implements Layouter {

    private final OrthogonalEdgeRouter _edgeRouter;

    private final Map<ProgramEntity, List<ProgramEntityRelationship>> _classNodeDependencyMap;
    private final Map<ProgramEntity, Boolean> _visitedNodes;
    private ImpactAnalysisGraph _impactAnalysisGraph;

    private static final double JavaPositionX = 0.0d;
    private static final double SwiftPositionX = 300.0d;
    private static final double GAP_Y = 80;


    public TraceabilityCompLayouter() {
        _edgeRouter = GraphManager.getGraphManager().createOrthogonalEdgeRouter();
        _edgeRouter.setGridRoutingEnabled(true);
        _edgeRouter.setGridSpacing(10);
        _edgeRouter.setCenterToSpaceRatio(0.0);
        _edgeRouter.setCoupledDistances(false);
        _edgeRouter.setMinimumDistanceToNode(20);

        _visitedNodes = new HashMap<>();
        _classNodeDependencyMap = new LinkedHashMap<>();

    }

    @Override
    public boolean canLayout(LayoutGraph layoutGraph) {
        return true;
    }

    @Override
    public void doLayout(LayoutGraph layoutGraph) {

        addDependencies(layoutGraph);
        calculateNodeLayout(layoutGraph);


        final GraphManager graphManager = GraphManager.getGraphManager();
        final PartitionGridRouterStage partitionGridRouterStage = graphManager.createPartitionGridRouterStage(_edgeRouter);
        final GroupNodeRouterStage groupNodeRouterStage = graphManager.createGroupNodeRouterStage(partitionGridRouterStage);
        final BufferedLayouter bufferedLayouter = graphManager.createBufferedLayouter(groupNodeRouterStage);

        bufferedLayouter.doLayout(layoutGraph);

    }

    /**
     * Sets the ClassGraph to provide information for the layout
     *
     * @param impactAnalysisGraph The ClassGraph for the LayoutGraph
     */
    public void setClassGraph(ImpactAnalysisGraph impactAnalysisGraph) {
        this._impactAnalysisGraph = impactAnalysisGraph;
    }

    /**
     * Places the CLassNodes
     * <p>
     * //@param layoutGraph the layoutgraph containing the nodes
     */
    private void calculateNodeLayout(LayoutGraph layoutGraph) {
        _visitedNodes.clear();
        final List<ProgramEntity[]> orderedProgramEntities = getOrderedProgramEntities();
        for (int i = 0; i < orderedProgramEntities.size(); i++) {
            ProgramEntity[] entities = orderedProgramEntities.get(i);
            placeNode(layoutGraph, entities[0], i);
            placeNode(layoutGraph, entities[1], i);
        }
    }


    /**
     * Places a ClassNode
     *
     * @param layoutGraph   The given LayoutGraph
     * @param programEntity The ProgramEntity that needs to be placed
     * @param ylevel        The ylevel of the "level"
     */
    private void placeNode(final LayoutGraph layoutGraph, ProgramEntity programEntity, int ylevel) {
        if (programEntity != null) {
            if (!nodeAlreadyPlaced(programEntity)) {
                moveTo(layoutGraph, programEntity, ylevel);
                setNodeAsPlaced(programEntity);
            }
        }
    }

    /**
     * Moves a ClassNode to his destinated coordinate
     *
     * @param layoutGraph   The current LayoutGraph
     * @param programEntity The ClassNode that has to move to a coordinate
     * @param yLevel        The level the ClassNode should move to
     */
    private void moveTo(LayoutGraph layoutGraph, ProgramEntity programEntity, int yLevel) {
        final double y = yLevel * GAP_Y;
        final double x = getXForClassNode(programEntity);
        final Node node = _impactAnalysisGraph.getNode(programEntity);

        final double vectorY = getVector(y, layoutGraph.getY(node));
        final double vectorX = getVector(x, layoutGraph.getX(node));

        layoutGraph.moveBy(node, vectorX, vectorY);
    }

    /**
     * Returns the X-Coordiante for a given ClassNode
     *
     * @param programEntity a ClassNode
     * @return the requested X-coordinate
     */
    private static double getXForClassNode(ProgramEntity programEntity) {
        if (programEntity.getLanguage() == Language.Java) {
            return JavaPositionX;
        } else {
            return SwiftPositionX;
        }
    }

    /**
     * Calculates the vector between the current position of a node and the destination
     *
     * @param destination  the destination to the Y-coordinate
     * @param nodePosition The current Y-positon of a node
     * @return the vector the node needs to be moved on the Y-axis
     */
    public static double getVector(double destination, double nodePosition) {
        return destination - nodePosition;
    }

    /**
     * Saves, if a node was already placed.
     *
     * @param programEntity the placed ClassNode
     */
    private void setNodeAsPlaced(ProgramEntity programEntity) {
        if (!_visitedNodes.containsKey(programEntity)) {
            _visitedNodes.put(programEntity, true);
        }
    }

    /**
     * Returns if given classNode was already placed before
     *
     * @param programEntity the requested ClassNode
     * @return returns true if already placed
     */
    private boolean nodeAlreadyPlaced(ProgramEntity programEntity) {
        if (_visitedNodes.containsKey(programEntity)) {
            return _visitedNodes.get(programEntity);
        }
        return false;
    }

    /**
     * Stores all dependencies and sorts them in a specific order
     *
     * @param graph the current LayoutGraph of the Layouter
     */
    private void addDependencies(LayoutGraph graph) {
        final Edge[] edgeArray = graph.getEdgeArray();
        for (Edge edge : edgeArray) {
            final ProgramEntityRelationship programEntityRelationship = _impactAnalysisGraph.getProgramEntityRelationship(edge);
            if (programEntityRelationship != null) {
                addNodeDependency(programEntityRelationship.getDependentClass(), programEntityRelationship);
                addNodeDependency(programEntityRelationship.getIndependentClass(), programEntityRelationship);
            }
        }
    }

    /**
     * Fills the classNodeDependencyMap and sorts the dependencies in a specific order
     *
     * @param node ClassNode as Key
     * @param edge A dependency associated with the ClassNode
     */
    private void addNodeDependency(final ProgramEntity node, final ProgramEntityRelationship edge) {
        _classNodeDependencyMap.computeIfAbsent(node, empList -> new ArrayList<>());
        List<ProgramEntityRelationship> nodeDependencies = _classNodeDependencyMap.get(node);
        if (!nodeDependencies.contains(edge)) {
            nodeDependencies.add(edge);
        }
        nodeDependencies.sort(new ClassDependencyComparator());
    }

    /**
     * Calculates an ordered Set of ClassNodes that need to be placed in the LayoutGraph in a specific order
     *
     * @return A non-null LinkedHashSet with ordered ClassNodes
     */
    private LinkedHashSet<ProgramEntity> getOrderedClassNodes(Language type) {
        final List<ProgramEntityRelationship> classDependencies = new ArrayList<>(_impactAnalysisGraph.getDataModel().getEdges());
        final LinkedHashSet<ProgramEntity> sortedNodes = new LinkedHashSet<>();
        final Language language = type;
        //Collections.sort(classDependencies);

        for (ProgramEntityRelationship programEntityRelationship : classDependencies) {
            final ProgramEntity dependentClass = programEntityRelationship.getDependentClass();
            final ProgramEntity independentClass = programEntityRelationship.getIndependentClass();
            if (dependentClass.getLanguage() == language) {
                sortedNodes.add(dependentClass);
            }
            if (independentClass.getLanguage() == language) {
                sortedNodes.add(independentClass);
            }
        }
        return sortedNodes;
    }

    public List<ProgramEntity[]> getOrderedProgramEntities() {
        final List<ProgramEntity[]> orderedEntities = new ArrayList<>();
        final List<ProgramEntityRelationship> relationsInLayoutGraph = new ArrayList<>(_impactAnalysisGraph.getDataModel().getEdges());
        final Set<ProgramEntity> placedProgramEntities = new HashSet<>();

        //Sort the relationships: First after traceabilty, then alphabetically
        Collections.sort(relationsInLayoutGraph, ProgramEntityRelationship.COMPARATOR);
        for (ProgramEntityRelationship relationship : relationsInLayoutGraph) {
            if (relationship instanceof TraceLinkProgramEntityAssociation) {
                ProgramEntity[] entities = new ProgramEntity[2];
                if (!placedProgramEntities.contains(relationship.getDependentClass())) {
                    entities[0] = relationship.getDependentClass();
                    placedProgramEntities.add(relationship.getDependentClass());

                }
                if (!placedProgramEntities.contains(relationship.getIndependentClass())) {
                    entities[1] = relationship.getIndependentClass();
                    placedProgramEntities.add(relationship.getIndependentClass());
                }
                orderedEntities.add(entities);
            } else {

                if (!placedProgramEntities.contains(relationship.getIndependentClass())) {
                    ProgramEntity[] entities = new ProgramEntity[2];
                    ProgramEntity entity1 = relationship.getIndependentClass();
                    if (entity1.getLanguage() == Language.Java) {
                        entities[0] = entity1;
                    }
                    if (entity1.getLanguage() == Language.Swift) {
                        entities[1] = entity1;
                    }
                    placedProgramEntities.add(entity1);
                    orderedEntities.add(entities);
                }
                if (!placedProgramEntities.contains(relationship.getDependentClass())) {
                    ProgramEntity[] entities = new ProgramEntity[2];
                    ProgramEntity entity2 = relationship.getDependentClass();
                    if (entity2.getLanguage() == Language.Java) {
                        entities[0] = entity2;
                    }
                    if (entity2.getLanguage() == Language.Swift) {
                        entities[1] = entity2;
                    }
                    placedProgramEntities.add(entity2);
                    orderedEntities.add(entities);
                }
            }

        }
        return orderedEntities;
    }
}


