package edu.csc413.tankgame.model;

import java.util.*;

/**
 * GameWorld holds all of the model objects present in the game. GameWorld tracks all moving entities like tanks and
 * shells, and provides access to this information for any code that needs it (such as GameDriver or entity classes).
 */

public class GameWorld {
    // TODO: Implement. There's a lot of information the GameState will need to store to provide contextual information.
    //       Add whatever instance variables, constructors, and methods are needed.

    //<tank> for now, will eventually be <entity>
    //private List<Tank> entities;
    private List<Entity> entities;
    private List<Entity> tempEntities;
    private List<Entity> garbageList;

    public GameWorld() {
        // TODO: Implement.
        entities = new ArrayList<>();
        tempEntities = new ArrayList<>();
        garbageList = new ArrayList<>();
    }

    /** Returns a list of all entities in the game. */
    public List<Entity> getEntities() {
        // TODO: Implement.
        return entities;
    }

//    public void addEntity(Tank entity) {
//        entities.add(entity);
//    }

    /** Adds a new entity to the game. */
    public void addEntity(Entity entity) {
        // TODO: Implement.
        entities.add(entity);
    }

    /** Returns the Entity with the specified ID. */
    public Entity getEntity(String id) {
        // TODO: Implement.
        return null;
    }

    /** Removes the entity with the specified ID from the game. */
    public void removeEntity(String id) {
        // TODO: Implement.
         // entities.remove(getEntity(id));
         // garbageList.remove(getGarbageList());
         garbageList.remove(getEntity(id));
    }

    // --------------------------------shells vv-----------------------------------------

    /** Adds a new entity to the game. */
    public void addShell(Entity shell) {
        tempEntities.add(shell);
    }

    public List<Entity> getTempEntities() {
        return tempEntities;
    }

    public void addToGarbage(Entity entity) {
        garbageList.add(entity);
    }

    public List<Entity> getGarbageList() {
        return garbageList;
    }

    public void removeShell(Entity entity) {
        entities.remove(entity);
        tempEntities.remove(entity);
        garbageList.remove(entity);
    }

//-----------------------------------------------------------------------------------

//
//    public List<Tank>getEntities() {
//        return entities;
//    }
//
//    public void addEntity(Tank entity) {
//        entities.add(entity);
//    }

//    private final List<Entity> tempList = new ArrayList<>();
//    public List<Entity> getShells() {
//        return tempList;
//    }

}
