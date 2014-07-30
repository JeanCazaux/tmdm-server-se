package com.amalto.core.query.optimization;

import com.amalto.core.query.user.*;
import com.amalto.core.query.user.metadata.*;
import com.amalto.core.storage.datasource.RDBMSDataSource;
import org.apache.commons.lang.NotImplementedException;
import org.apache.log4j.Logger;
import org.talend.mdm.commmon.metadata.MetadataRepository;
import org.talend.mdm.commmon.metadata.TypeMetadata;

public class IncompatibleOperators implements Optimizer {

    private static final Logger LOGGER = Logger.getLogger(IncompatibleOperators.class);

    // Transformer for SQL server incompatible operators
    private static final SQLServerIncompatibleOperators SQL_SERVER = new SQLServerIncompatibleOperators();

    private final RDBMSDataSource dataSource;

    public IncompatibleOperators(RDBMSDataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Override
    public void optimize(Select select) {
        Visitor<Condition> transformer = getIncompatibleOperatorTransformer(dataSource);
        if (transformer != null) {
            if (LOGGER.isDebugEnabled()) {
                LOGGER.debug("Datasource (dialect " + dataSource.getDialectName() + ") may require operator replacements.");
            }
            Condition selectCondition = select.getCondition();
            if (selectCondition != null) {
                select.setCondition(selectCondition.accept(transformer));
            }
        }
    }

    private static Visitor<Condition> getIncompatibleOperatorTransformer(RDBMSDataSource dataSource) {
        RDBMSDataSource.DataSourceDialect dialect = dataSource.getDialectName();
        switch (dialect) {
        case ORACLE_10G:
        case MYSQL:
        case POSTGRES:
        case H2:
        case DB2:
            return null;
        case SQL_SERVER:
            // TMDM-7532: SQL Server does not like equals operator on large text values
            return SQL_SERVER;
        default:
            throw new NotImplementedException("Dialect '" + dialect + "' is not implemented.");
        }
    }

    private static class SQLServerIncompatibleOperators implements Visitor<Condition> {

        private Field currentField;

        @Override
        public Condition visit(Select select) {
            return select.getCondition().accept(this);
        }

        @Override
        public Condition visit(StringConstant constant) {
            return null;
        }

        @Override
        public Condition visit(Isa isa) {
            return isa;
        }

        @Override
        public Condition visit(UnaryLogicOperator condition) {
            return condition;
        }

        @Override
        public Condition visit(IsEmpty isEmpty) {
            return isEmpty;
        }

        @Override
        public Condition visit(IsNull isNull) {
            return isNull;
        }

        @Override
        public Condition visit(NotIsEmpty notIsEmpty) {
            return notIsEmpty;
        }

        @Override
        public Condition visit(NotIsNull notIsNull) {
            return notIsNull;
        }

        @Override
        public Condition visit(Field field) {
            currentField = field;
            return null;
        }

        @Override
        public Condition visit(BinaryLogicOperator condition) {
            condition.setLeft(condition.getLeft().accept(this));
            condition.setRight(condition.getRight().accept(this));
            return condition;
        }

        @Override
        public Condition visit(Compare condition) {
            Predicate predicate = condition.getPredicate();
            if (predicate == Predicate.EQUALS) {
                Expression left = condition.getLeft();
                Expression right = condition.getRight();
                left.accept(this);
                right.accept(this);
                TypeMetadata fieldType = currentField.getFieldMetadata().getType();
                if (fieldType.getData(MetadataRepository.DATA_MAX_LENGTH) != null) {
                    if (LOGGER.isDebugEnabled()) {
                        LOGGER.debug("Replacing EQUALS with STARTS_WITH (can't use EQUALS on large text column).");
                    }
                    return new Compare(left, Predicate.STARTS_WITH, right);
                }
            }
            return condition;
        }

        @Override
        public Condition visit(Range range) {
            return range;
        }

        @Override
        public Condition visit(Condition condition) {
            return condition;
        }

        @Override
        public Condition visit(FullText fullText) {
            return fullText;
        }

        @Override
        public Condition visit(FieldFullText fieldFullText) {
            return fieldFullText;
        }

        @Override
        public Condition visit(NativeQuery nativeQuery) {
            return null;
        }

        @Override
        public Condition visit(Max max) {
            return null;
        }

        @Override
        public Condition visit(Min min) {
            return null;
        }

        @Override
        public Condition visit(ConstantCondition constantCondition) {
            return constantCondition;
        }

        @Override
        public Condition visit(Timestamp timestamp) {
            return null;
        }

        @Override
        public Condition visit(TaskId taskId) {
            return null;
        }

        @Override
        public Condition visit(Type type) {
            return null;
        }

        @Override
        public Condition visit(Distinct distinct) {
            return null;
        }

        @Override
        public Condition visit(StagingStatus stagingStatus) {
            return null;
        }

        @Override
        public Condition visit(StagingError stagingError) {
            return null;
        }

        @Override
        public Condition visit(StagingSource stagingSource) {
            return null;
        }

        @Override
        public Condition visit(StagingBlockKey stagingBlockKey) {
            return null;
        }

        @Override
        public Condition visit(GroupSize groupSize) {
            return null;
        }

        @Override
        public Condition visit(Join join) {
            return null;
        }

        @Override
        public Condition visit(Expression expression) {
            return null;
        }

        @Override
        public Condition visit(Predicate predicate) {
            return null;
        }

        @Override
        public Condition visit(Alias alias) {
            return null;
        }

        @Override
        public Condition visit(Id id) {
            return null;
        }

        @Override
        public Condition visit(ConstantCollection collection) {
            return null;
        }

        @Override
        public Condition visit(IntegerConstant constant) {
            return null;
        }

        @Override
        public Condition visit(DateConstant constant) {
            return null;
        }

        @Override
        public Condition visit(DateTimeConstant constant) {
            return null;
        }

        @Override
        public Condition visit(BooleanConstant constant) {
            return null;
        }

        @Override
        public Condition visit(BigDecimalConstant constant) {
            return null;
        }

        @Override
        public Condition visit(TimeConstant constant) {
            return null;
        }

        @Override
        public Condition visit(ShortConstant constant) {
            return null;
        }

        @Override
        public Condition visit(ByteConstant constant) {
            return null;
        }

        @Override
        public Condition visit(LongConstant constant) {
            return null;
        }

        @Override
        public Condition visit(DoubleConstant constant) {
            return null;
        }

        @Override
        public Condition visit(FloatConstant constant) {
            return null;
        }

        @Override
        public Condition visit(Predicate.And and) {
            return null;
        }

        @Override
        public Condition visit(Predicate.Or or) {
            return null;
        }

        @Override
        public Condition visit(Predicate.Equals equals) {
            return null;
        }

        @Override
        public Condition visit(Predicate.Contains contains) {
            return null;
        }

        @Override
        public Condition visit(OrderBy orderBy) {
            return null;
        }

        @Override
        public Condition visit(Paging paging) {
            return null;
        }

        @Override
        public Condition visit(Count count) {
            return null;
        }

        @Override
        public Condition visit(Predicate.GreaterThan greaterThan) {
            return null;
        }

        @Override
        public Condition visit(Predicate.LowerThan lowerThan) {
            return null;
        }

        @Override
        public Condition visit(At at) {
            return null;
        }

        @Override
        public Condition visit(ComplexTypeExpression expression) {
            return null;
        }

        @Override
        public Condition visit(IndexedField indexedField) {
            return null;
        }
    }
}
