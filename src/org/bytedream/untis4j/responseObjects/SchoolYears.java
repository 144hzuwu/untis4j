package org.bytedream.untis4j.responseObjects;

import org.bytedream.untis4j.responseObjects.baseObjects.BaseResponseLists.ResponseList;
import org.bytedream.untis4j.responseObjects.baseObjects.BaseResponseObjects.ResponseObject;
import org.json.JSONObject;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;

/**
 * Class to manage {@link SchoolYearObject} objects
 *
 * @version 1.0
 * @since 1.0
 */
public class SchoolYears extends ResponseList<SchoolYears.SchoolYearObject> {

    /**
     * Finds a school year by its name
     *
     * @param name name of the school year you want to find
     * @return the school year
     *
     * @since 1.0
     */
    public SchoolYearObject findByName(String name) {
        return this.stream().filter(schoolYearObject -> schoolYearObject.getName().equals(name)).findAny().orElse(null);
    }

    /**
     * Finds a school year by its start date
     *
     * @param startDate start date of the school year you want to find
     * @return the school year
     *
     * @since 1.0
     */
    public SchoolYearObject findByStartDate(LocalDate startDate) {
        return this.stream().filter(schoolYearObject -> schoolYearObject.getStartDate().isEqual(startDate)).findAny().orElse(null);
    }

    /**
     * Finds a school year by its end date
     *
     * @param endDate end date of the school year you want to find
     * @return the school year
     *
     * @since 1.0
     */
    public SchoolYearObject findByEndDate(LocalDate endDate) {
        return this.stream().filter(schoolYearObject -> schoolYearObject.getStartDate().isEqual(endDate)).findAny().orElse(null);
    }

    /**
     * Finds a school year by its id
     *
     * @param id id of the school year you want to find
     * @return the school year
     *
     * @since 1.0
     */
    public SchoolYearObject findById(int id) {
        return this.stream().filter(schoolYearObject -> schoolYearObject.getId() == id).findAny().orElse(null);
    }

    /**
     * Finds school years that have the {@code name} or a part of it in their name
     *
     * @param name name of the school years you want to search
     * @return {@link SchoolYears} with school years that have the {@code name} or a part of it in their name
     *
     * @since 1.0
     */
    public SchoolYears searchByName(String name) {
        SchoolYears schoolYears = new SchoolYears();

        this.stream().filter(schoolYearObject -> schoolYearObject.getName().contains(name)).forEach(schoolYears::add);

        return schoolYears;
    }

    /**
     * Finds school years that have the {@code startDate} or a part of it in their start date
     *
     * @param startDate start date of the school years you want to search
     * @return {@link SchoolYears} with school years that have the {@code startDate} or a part of it in their start date
     *
     * @since 1.0
     */
    public SchoolYears searchByStartDate(LocalDate startDate) {
        SchoolYears schoolYears = new SchoolYears();

        this.stream().filter(schoolYearObject -> schoolYearObject.getStartDate().format(DateTimeFormatter.ofPattern("yyyyMMdd")).contains(startDate.format(DateTimeFormatter.ofPattern("yyyyMMdd")))).forEach(schoolYears::add);

        return schoolYears;
    }

    /**
     * Finds school years that have the {@code endDate} or a part of it in their end date
     *
     * @param endDate end date of the school years you want to search
     * @return {@link SchoolYears} with school years that have the {@code endDate} or a part of it in their end date
     *
     * @since 1.0
     */
    public SchoolYears searchByEndDate(LocalDate endDate) {
        SchoolYears schoolYears = new SchoolYears();

        this.stream().filter(schoolYearObject -> schoolYearObject.getEndDate().format(DateTimeFormatter.ofPattern("yyyyMMdd")).contains(endDate.format(DateTimeFormatter.ofPattern("yyyyMMdd")))).forEach(schoolYears::add);

        return schoolYears;
    }

    /**
     * Finds school years that have the {@code id} or a part of it in their id
     *
     * @param id id of the school years you want to search
     * @return {@link SchoolYears} with school years that have the {@code id} or a part of it in their id
     *
     * @since 1.0
     */
    public SchoolYears searchById(int id) {
        SchoolYears schoolYears = new SchoolYears();

        this.stream().filter(schoolYearObject -> String.valueOf(schoolYearObject.getId()).contains(String.valueOf(id))).forEach(schoolYears::add);

        return schoolYears;
    }

    /**
     * Class to get information about the a school year
     *
     * @version 1.0
     * @since 1.0
     */
    public static class SchoolYearObject extends ResponseObject {

        private final String name;
        private final LocalDate startDate;
        private final LocalDate endDate;
        private final int id;

        /**
         * Initialize the {@link SchoolYearObject} class
         *
         * @param name name of the department
         * @param startDate date when the school year began
         * @param endDate date when the school year will end
         *
         * @since 1.0
         */
        public SchoolYearObject(String name, LocalDate startDate, LocalDate endDate, int id) {
            this.name = name;
            this.startDate = startDate;
            this.endDate = endDate;
            this.id = id;
        }

        /**
         * Returns the name of the school year
         *
         * @return the name of the school year
         *
         * @since 1.0
         */
        public String getName() {
            return name;
        }

        /**
         * Returns the start day of the school year
         *
         * @return the start day of the school year
         *
         * @since 1.0
         */
        public LocalDate getStartDate() {
            return startDate;
        }

        /**
         * Returns the end day of the school year
         *
         * @return the end day of the school year
         *
         * @since 1.0
         */
        public LocalDate getEndDate() {
            return endDate;
        }

        /**
         * Returns the id of the school year
         *
         * @return the id of the school year
         *
         * @since 1.0
         */
        public int getId() {
            return id;
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
            HashMap<String, Object> currentSchoolYearAsMap = new HashMap<>();

            currentSchoolYearAsMap.put("name", name);
            currentSchoolYearAsMap.put("startDate", startDate);
            currentSchoolYearAsMap.put("endDate", endDate);

            return new JSONObject(currentSchoolYearAsMap).toString();
        }
    }

}

