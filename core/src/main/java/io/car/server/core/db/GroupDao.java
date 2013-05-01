/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package io.car.server.core.db;

import io.car.server.core.User;
import io.car.server.core.Group;
import io.car.server.core.Groups;

/**
 *
 * @author Christian Autermann <c.autermann@52north.org>
 */
public interface GroupDao {
    Group getByName(String name);
    Groups search(String search);
    Groups getByOwner(User owner);
    Groups getAll(int limit);
    Group create(Group group);
    Group save(Group group);
    void delete(Group group);
}
