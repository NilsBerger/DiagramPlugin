/*
 * Copyright 1998-2018 Konstantin Bulenkov
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package graphapi;

import com.intellij.openapi.graph.GraphManager;
import com.intellij.openapi.graph.base.Edge;
import com.intellij.openapi.graph.base.EdgeCursor;
import com.intellij.openapi.graph.base.EdgeList;
import com.intellij.openapi.graph.base.Node;
import com.intellij.openapi.graph.geom.YPoint;
import com.intellij.openapi.graph.layout.EdgeLayout;
import com.intellij.openapi.graph.layout.LayoutGraph;
import com.intellij.openapi.graph.layout.Layouter;

import java.util.Comparator;

public class CustomGeneralLayouter implements Layouter {
    private double minimalNodeDistance = 40;
    private GraphManager manager;

    public CustomGeneralLayouter()
    {
        manager = GraphManager.getGraphManager();
    }

    public void setMinimalNodeDistance(double d)
    {
        minimalNodeDistance = d;
    }

    public double getMinimalNodeDistance()
    {
        return minimalNodeDistance;
    }

    public boolean canLayout(LayoutGraph graph)
    {
        return true;
    }

    public void doLayout(LayoutGraph graph)
    {
        Node nodes[] = graph.getNodeArray();
        double offset = 0.0;
        for(int i = 0; i < nodes.length; i++)
        {
            Node v = nodes[i];
            graph.setLocation(v,offset,offset);
            offset += minimalNodeDistance + Math.max(graph.getWidth(v),graph.getHeight(v));
        }

        //comparator used to sort edges by the
        //index of their target node
        Comparator outComp = new Comparator() {
            public int compare(Object a, Object b) {
                Node va = ((Edge)a).target();
                Node vb = ((Edge)b).target();
                if(va != vb)
                    return va.index() - vb.index();
                else
                    return ((Edge)a).index() - ((Edge)b).index();
            }
        };

        //comparator used to sort edges by the
        //index of their source node.
        Comparator inComp = new Comparator() {
            public int compare(Object a, Object b) {
                Node va = ((Edge)a).source();
                Node vb = ((Edge)b).source();
                if(va != vb)
                    return va.index() - vb.index();
                else
                    return ((Edge)b).index() - ((Edge)a).index();
            }
        };

        //prepare edge layout. use exactly one bend per edge
//        for(EdgeCursor ec = graph.edges(); ec.ok(); ec.next())
//        {
//            EdgeLayout el = graph.getLayout(ec.edge());
//            el.clearPoints();
//            el.addPoint(0,0);
//        }

        //route the edges
        for(int i = 0; i < nodes.length; i++)
        {
            Node v = nodes[i];

            EdgeList rightSide  = manager.createEdgeList();
            EdgeList leftSide   = manager.createEdgeList();

            //assign x coodinates to all outgoing edges of v
            v.sortOutEdges(outComp);
            for(EdgeCursor ec = v.outEdges(); ec.ok(); ec.next())
            {
                Edge e = ec.edge();
                Node w = e.target();

                if(w.index() < v.index())
                    rightSide.addLast(e);
                else
                    leftSide.addLast(e);
            }

            if(!rightSide.isEmpty())
            {
                double space  = graph.getWidth(v)/rightSide.size();
                double xcoord = graph.getX(v) + graph.getWidth(v) - space/2.0;
                for(EdgeCursor ec = rightSide.edges(); ec.ok(); ec.next())
                {
                    Edge e = ec.edge();
                    EdgeLayout el = graph.getLayout(e);
                    YPoint p = el.getPoint(0);
                    el.setPoint(0, xcoord, p.getY());
                    graph.setSourcePointAbs(e, manager.createYPoint(xcoord, graph.getCenterY(v)));
                    xcoord -= space;
                }
            }

            if(!leftSide.isEmpty())
            {
                double space  = graph.getWidth(v)/leftSide.size();
                double xcoord = graph.getX(v) + graph.getWidth(v) - space/2.0;
                for(EdgeCursor ec = leftSide.edges(); ec.ok(); ec.next())
                {
                    Edge e = ec.edge();
                    EdgeLayout el = graph.getLayout(e);
                    YPoint p = el.getPoint(0);
                    el.setPoint(0, xcoord, p.getY());
                    graph.setSourcePointAbs(e, manager.createYPoint(xcoord,graph.getCenterY(v)));
                    xcoord -= space;
                }
            }

            //assign y coodinates to all ingoing edges of v
            rightSide.clear();
            leftSide.clear();
            v.sortInEdges(inComp);
            for(EdgeCursor ec = v.inEdges(); ec.ok(); ec.next())
            {
                Edge e = ec.edge();
                Node w = e.source();

                if(w.index() < v.index())
                    leftSide.addLast(e);
                else
                    rightSide.addLast(e);
            }

            if(!rightSide.isEmpty())
            {
                double space  = graph.getHeight(v)/rightSide.size();
                double ycoord = graph.getY(v) + graph.getHeight(v) - space/2.0;
                for(EdgeCursor ec = rightSide.edges(); ec.ok(); ec.next())
                {
                    Edge e = ec.edge();
                    EdgeLayout el = graph.getLayout(e);
                    YPoint p = el.getPoint(0);
                    el.setPoint(0, p.getX(), ycoord);
                    graph.setTargetPointAbs(e, manager.createYPoint(graph.getCenterX(v), ycoord));
                    ycoord -= space;
                }
            }

            if(!leftSide.isEmpty())
            {
                double space  = graph.getHeight(v)/leftSide.size();
                double ycoord = graph.getY(v) + graph.getHeight(v) - space/2.0;
                for(EdgeCursor ec = leftSide.edges(); ec.ok(); ec.next())
                {
                    Edge e = ec.edge();
                    EdgeLayout el = graph.getLayout(e);
                    YPoint p = el.getPoint(0);
                    el.setPoint(0, p.getX(), ycoord);
                    graph.setTargetPointAbs(e, manager.createYPoint(graph.getCenterX(v), ycoord));
                    ycoord -= space;
                }
            }
        }
    }
}
