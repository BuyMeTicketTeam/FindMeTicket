package com.booking.app.entities.user.types;

import com.booking.app.constants.AuthProvider;
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
public class ColumnAuthProviderType implements UserType<AuthProvider[]> {

    private static final int SQL_TYPE = Types.ARRAY;

    private final DatabaseSchemaProps schemaProperties;

    @Override
    public int getSqlType() {
        return SQL_TYPE;
    }

    @Override
    public Class<AuthProvider[]> returnedClass() {
        return AuthProvider[].class;
    }

    @Override
    public boolean equals(AuthProvider[] x, AuthProvider[] y) {
        return Arrays.equals(x, y);
    }

    @Override
    public int hashCode(AuthProvider[] x) {
        return Arrays.hashCode(x);
    }

    @Override
    public AuthProvider[] nullSafeGet(ResultSet rs, int position, SharedSessionContractImplementor session, Object owner) throws SQLException {
        Array sqlArray = rs.getArray(position);
        if (sqlArray == null) {
            log.warn("Null array returned for column auth provider type");
            return new AuthProvider[0];
        }
        String[] stringArray = (String[]) sqlArray.getArray();
        return Arrays.stream(stringArray)
                .map(AuthProvider::valueOf)
                .toArray(AuthProvider[]::new);
    }

    @Override
    public void nullSafeSet(PreparedStatement st, AuthProvider[] value, int index, SharedSessionContractImplementor session) throws SQLException {
        if (st != null) {
            if (value != null) {
                String[] stringArray = Arrays.stream(value)
                        .map(Enum::name)
                        .toArray(String[]::new);
                try (Connection connection = session.getJdbcConnectionAccess().obtainConnection()) {
                    String enumType = schemaProperties.getSchema().concat(".auth_provider");
                    st.setArray(index, connection.createArrayOf(enumType, stringArray));
                    log.debug("Set auth provider array to {}", Arrays.toString(stringArray));
                }
            } else {
                log.debug("Value of provider is null, setting PreparedStatement parameter to NULL");
                st.setNull(index, SQL_TYPE);
            }
        } else {
            log.error("PreparedStatement is null");
            throw new SQLException("PreparedStatement is null");
        }
    }

    @Override
    public AuthProvider[] deepCopy(AuthProvider[] value) {
        return value != null ? value.clone() : null;
    }

    @Override
    public boolean isMutable() {
        return true;
    }

    @Override
    public Serializable disassemble(AuthProvider[] value) {
        return deepCopy(value);
    }

    @Override
    public AuthProvider[] assemble(Serializable cached, Object owner) {
        return deepCopy((AuthProvider[]) cached);
    }

    @Override
    public AuthProvider[] replace(AuthProvider[] original, AuthProvider[] target, Object owner) {
        return deepCopy(original);
    }


}
