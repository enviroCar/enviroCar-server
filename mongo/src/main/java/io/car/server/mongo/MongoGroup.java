/**
 * Copyright (C) 2013
 * by 52 North Initiative for Geospatial Open Source Software GmbH
 *
 * Contact: Andreas Wytzisk
 * 52 North Initiative for Geospatial Open Source Software GmbH
 * Martin-Luther-King-Weg 24
 * 48155 Muenster, Germany
 * info@52north.org
 *
 * This program is free software; you can redistribute and/or modify it under
 * the terms of the GNU General Public License version 2 as published by the
 * Free Software Foundation.
 *
 * This program is distributed WITHOUT ANY WARRANTY; even without the implied
 * WARRANTY OF MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License along with
 * this program (see gnu-gpl v2.txt). If not, write to the Free Software
 * Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA 02111-1307, USA or
 * visit the Free Software Foundation web page, http://www.fsf.org.
 */

package io.car.server.mongo;

import java.util.Set;

import com.github.jmkgreen.morphia.annotations.Entity;
import com.github.jmkgreen.morphia.annotations.Indexed;
import com.github.jmkgreen.morphia.annotations.PreSave;
import com.github.jmkgreen.morphia.annotations.Property;
import com.github.jmkgreen.morphia.annotations.Reference;

import io.car.server.core.Group;
import io.car.server.core.User;
import io.car.server.core.Users;

/**
 * @author Christian Autermann <c.autermann@52north.org>
 */
@Entity("userGroups")
public class MongoGroup extends MongoBaseEntity implements Group {
    public static final String NAME = "name";
    public static final String DESCRIPTION = "desc";
    public static final String MEMBERS = "members";
    public static final String OWNER = "owner";
    @Indexed(unique = true)
    @Property(NAME)
    private String name;
    @Property(DESCRIPTION)
    private String description;
    @Reference(value = MEMBERS, lazy = true)
    private Set<MongoUser> members;
    @Reference(value = OWNER, lazy = true)
    private MongoUser owner;

    @Override
    public MongoGroup setName(String name) {
        this.name = name;
        return this;
    }

    @Override
    public String getName() {
        return this.name;
    }

    @Override
    public MongoGroup setDescription(String description) {
        this.description = description;
        return this;
    }

    @Override
    public String getDescription() {
        return this.description;
    }

    @Override
    public Users getMembers() {
        return new Users(this.members);
    }

    @Override
    public MongoGroup addMember(User user) {
        this.members.add((MongoUser) user);
        return this;
    }

    @Override
    public MongoGroup removeMember(User user) {
        this.members.remove((MongoUser) user);
        return this;
    }

    @Override
    public MongoGroup setOwner(User user) {
        this.owner = (MongoUser) user;
        return this;
    }

    @Override
    public MongoUser getOwner() {
        return this.owner;
    }

    @PreSave
    public void clearMemberChanges() {
    }
}
