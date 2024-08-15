package com.booking.app.entities.user.types;

import com.booking.app.constants.RoleType;
import com.booking.app.properties.DatabaseSchemaProps;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.hibernate.engine.spi.SharedSessionContractImplementor;
import org.hibernate.usertype.UserType;
import org.springframework.stereotype.Component;

import java.io.Serializable;
import java.sql.*;
import java.util.Arrays;


@Component
@RequiredArgsConstructor
@Log4j2
public class ColumnRoleType implements UserType<RoleType[]> {

    private static final int SQL_TYPE = Types.ARRAY;

    private final DatabaseSchemaProps schemaProperties;

    @Override
    public int getSqlType() {
        return SQL_TYPE;
    }

    @Override
    public Class<RoleType[]> returnedClass() {
        return RoleType[].class;
    }

    @Override
    public boolean equals(RoleType[] x, RoleType[] y) {
        return Arrays.equals(x, y);
    }

    @Override
    public int hashCode(RoleType[] x) {
        return Arrays.hashCode(x);
    }

    @Override
    public RoleType[] nullSafeGet(ResultSet rs, int position, SharedSessionContractImplementor session, Object owner) throws SQLException {
        Array sqlArray = rs.getArray(position);
        if (sqlArray == null) {
            log.warn("Null array returned for column role type");
            return new RoleType[0];
        }
        String[] stringArray = (String[]) sqlArray.getArray();
        return Arrays.stream(stringArray)
                .map(RoleType::valueOf)
                .toArray(RoleType[]::new);
    }

    @Override
    public void nullSafeSet(PreparedStatement st, RoleType[] value, int index, SharedSessionContractImplementor session) throws SQLException {
        if (st != null) {
            if (value != null) {
                String[] stringArray = Arrays.stream(value)
                        .map(Enum::name)
                        .toArray(String[]::new);
                try (Connection connection = session.getJdbcConnectionAccess().obtainConnection()) {
                    String enumType = schemaProperties.getSchema().concat(".role");
                    st.setArray(index, connection.createArrayOf(enumType, stringArray));
                    log.debug("Set role array to {}", Arrays.toString(stringArray));
                }
            } else {
                log.debug("Value of role type is null, setting PreparedStatement parameter to NULL");
                st.setNull(index, SQL_TYPE);
            }
        } else {
            log.error("PreparedStatement is null");
            throw new SQLException("PreparedStatement is null");
        }
    }

    @Override
    public RoleType[] deepCopy(RoleType[] value) {
        return value != null ? value.clone() : null;
    }

    @Override
    public boolean isMutable() {
        return true;
    }

    @Override
    public Serializable disassemble(RoleType[] value) {
        return deepCopy(value);
    }

    @Override
    public RoleType[] assemble(Serializable cached, Object owner) {
        return deepCopy((RoleType[]) cached);
    }

    @Override
    public RoleType[] replace(RoleType[] original, RoleType[] target, Object owner) {
        return deepCopy(original);
    }

}