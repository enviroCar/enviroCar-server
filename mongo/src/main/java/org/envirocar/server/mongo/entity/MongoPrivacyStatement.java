package org.envirocar.server.mongo.entity;

import com.google.common.base.Objects;
import org.bson.types.ObjectId;
import org.envirocar.server.core.entities.PrivacyStatement;
import org.mongodb.morphia.annotations.Entity;
import org.mongodb.morphia.annotations.Id;
import org.mongodb.morphia.annotations.Property;
import org.mongodb.morphia.mapping.Mapper;

@Entity("privacyStatements")
public class MongoPrivacyStatement extends MongoEntityBase implements PrivacyStatement {
    public static final String NAME = Mapper.ID_KEY;
    public static final String CONTENTS = "contents";
    public static final String DATE_STRING = "issuedDate";

    @Id
    private ObjectId id = new ObjectId();

    @Property(DATE_STRING)
    private String issuedDate;

    @Property(CONTENTS)
    private String contents;

    @Override
    public String getIdentifier() {
        return this.id == null ? null : this.id.toString();
    }

    @Override
    public void setIdentifier(String id) {
        this.id = id == null ? null : new ObjectId(id);
    }

    @Override
    public String getIssuedDate() {
        return this.issuedDate;
    }

    @Override
    public void setIssuedDate(String ds) {
        this.issuedDate = ds;
    }

    @Override
    public String getContents() {
        return this.contents;
    }

    @Override
    public void setContents(String c) {
        this.contents = c;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(this.issuedDate);
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final MongoPrivacyStatement other = (MongoPrivacyStatement) obj;
        return Objects.equal(this.issuedDate, other.issuedDate);
    }
}
