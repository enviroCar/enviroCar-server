package org.envirocar.server.core.entities;

public interface Terms extends BaseEntity {

    String getIssuedDate();

    void setIssuedDate(String ds);

    String getContents();

    void setContents(String c);

    String getIdentifier();

    void setIdentifier(String id);

}
