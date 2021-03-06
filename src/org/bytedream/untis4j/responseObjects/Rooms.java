package org.bytedream.untis4j.responseObjects;

import org.bytedream.untis4j.responseObjects.baseObjects.BaseResponseLists.NAILResponseList;
import org.bytedream.untis4j.responseObjects.baseObjects.BaseResponseObjects.NAILResponseObject;
import org.json.JSONObject;

import java.util.HashMap;

/**
 * Class to manage {@link RoomObject} objects
 *
 * @version 1.0
 * @since 1.0
 */
public class Rooms extends NAILResponseList<Rooms.RoomObject> {

    /**
     * Finds an building by its name
     *
     * @param building name of the building you want to find
     * @return the building
     *
     * @since 1.0
     */
    public RoomObject findByBuilding(String building) {
        return this.stream().filter(roomObject -> roomObject.getBuilding().equals(building)).findAny().orElse(null);
    }

    /**
     * Finds buildings that have the {@code building} name or a part of it in their name
     *
     * @param building name of the buildings you want to search
     * @return {@link Rooms} with buildings that have the {@code building} name or a part of it in their name
     *
     * @since 1.0
     */
    public Rooms searchByBuilding(String building) {
        Rooms rooms = new Rooms();

        this.stream().filter(roomObject -> roomObject.getBuilding().equals(building)).forEach(rooms::add);

        return rooms;
    }

    /**
     * Class to get information about a room
     *
     * @version 1.0
     * @since 1.0
     */
    public static class RoomObject extends NAILResponseObject {

        private final String building;

        /**
         * Initialize the {@link RoomObject} class
         *
         * @param name name of the room
         * @param active if the room is active
         * @param id id of the room
         * @param building building in which the room is (i think)
         * @param longName long name of the room
         *
         * @since 1.0
         */
        public RoomObject(String name, boolean active, int id, String building, String longName) {
            super(name, active, id, longName);
            this.building = building;
        }

        /**
         * Returns the building in which the room is
         *
         * @return the building in which the room is
         *
         * @since 1.0
         */
        public String getBuilding() {
            return building;
        }

        /**
         * Returns a json parsed string with all information
         *
         * @return a json parsed string with all information
         *
         * @since 1.0
         */
        @Override
        public String toString() {
            HashMap<String, String> teacherAsMap = new HashMap<>();

            teacherAsMap.put("name", this.getName());
            teacherAsMap.put("isActive", String.valueOf(this.isActive()));
            teacherAsMap.put("id", String.valueOf(this.getId()));
            teacherAsMap.put("building", building);
            teacherAsMap.put("longName", this.getLongName());

            return new JSONObject(teacherAsMap).toString();
        }
    }

}
