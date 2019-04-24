package valueobjects;

/**
 * Defines the types of relationsships a dependency betweeen two ClassNodes can have
 */
public enum RelationshipType {
    Dependency, Traceability_Association, InconsistentRelationship, Association, Directed_Association, Aggregation, Composition, Extends, Implements, NEW_EXPRESSION, FIELD_TYPE_MANY, FIELD_TYPE_ONE
}

