/*
 * @author blueShard
 * @version 1.0
 */

package org.bytedream.untis4j;

import org.bytedream.untis4j.responseObjects.*;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.time.DateTimeException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.*;

public class Session {

    private final Infos infos;

    private RequestManager requestManager;

    /**
     * Class to do all the Untis stuff.
     *
     * <p>Main class to handle and get all Untis information<p/>
     *
     * @param infos infos about the user
     * @param requestManager manager that handles all requests
     *
     * @since 1.0
     */
    private Session(Infos infos, RequestManager requestManager) {
        this.infos = infos;
        this.requestManager = requestManager;
    }

    /**
     * Information about the Request remark categories (<- from https://github.com/python-webuntis/python-webuntis).
     *
     * <p>Requests all class reg categories and give {@link Response} with all information about from the request back (i got an error while testing this method: no right for getClassregCategories())</p>
     *
     * @return {@link Response} with the response from the request
     * @throws IOException if an IO Exception occurs
     *
     * @since 1.0
     */
    public Response getClassRegCategories() throws IOException {
        Response response = requestManager.POST(UntisUtils.Methods.GETCLASSREGCATEGORIES.getMethod());

        if (response.isError()) {
            throw new IOException(response.getErrorMessage());
        }

        return response;
    }

    /**
     * Information about the Request remark categories groups (<- from https://github.com/python-webuntis/python-webuntis).
     *
     * <p>Requests all class reg categories and give {@link Response} with all information about from the request back (i got an error while testing this method: no right for getClassregCategoryGroups())</p>
     *
     * @return {@link Response} with the response from the request
     * @throws IOException if an IO Exception occurs
     *
     * @since 1.0
     */
    public Response getClassRegCategoryGroups() throws IOException {
        Response response = requestManager.POST(UntisUtils.Methods.GETCLASSREGCATEGORYGROUPS.getMethod());

        if (response.isError()) {
            throw new IOException(response.getErrorMessage());
        }

        return response;
    }

    /**
     * Get the Information about the ClassRegEvents for a specific school class and time period (<- from https://github.com/python-webuntis/python-webuntis).
     *
     * <p>Requests all class reg categories and give {@link Response} with all information about from the request back (i got an error while testing this method: no right for getClassregEvents())</p>
     *
     * @param start the beginning of the time period
     * @param end the end of the time period
     * @param elementType type on which the timetable should be oriented
     * @param id id of the {@code elementType}
     * @return {@link Response} with the response from the request
     * @throws IOException if an IO Exception occurs
     *
     * @since 1.0
     */
    public Response getClassRegEvents(LocalDate start, LocalDate end, UntisUtils.ElementType elementType, Integer id) throws IOException {
        if (start.isBefore(end)) {
            throw new DateTimeException("The start date must end after or on the same day as the end date");
        }

        HashMap<String, Object> params = new HashMap<>();

        params.put("startDate", start.format(DateTimeFormatter.ofPattern("yyyyMMdd")));
        params.put("endDate", end.format(DateTimeFormatter.ofPattern("yyyyMMdd")));

        if (elementType != null && id != null) {
            params.put("type", elementType.getElementType());
            params.put("id", id);
        }

        Response response = requestManager.POST(UntisUtils.Methods.GETCLASSREGEVENTS.getMethod(), params);

        if (response.isError()) {
            throw new IOException(response.getErrorMessage());
        }

        return response;
    }

    /**
     * Get the Information about the ClassRegEvents for a specific time period and klasse id (<- from https://github.com/python-webuntis/python-webuntis).
     *
     * @see Session#getClassRegEvents(LocalDate, LocalDate, UntisUtils.ElementType, Integer)
     *
     * @since 1.0
     */
    public Response getAllClassRegEventsFromKlasseId(LocalDate start, LocalDate end, int id) throws IOException {
        return this.getClassRegEvents(start, end, UntisUtils.ElementType.KLASSE, id);
    }

    /**
     * Get the Information about the ClassRegEvents for a specific time period and teacher id (<- from https://github.com/python-webuntis/python-webuntis).
     *
     * @see Session#getClassRegEvents(LocalDate, LocalDate, UntisUtils.ElementType, Integer)
     *
     * @since 1.0
     */
    public Response getAllClassRegEventsFromTeacherId(LocalDate start, LocalDate end, int id) throws IOException {
        return this.getClassRegEvents(start, end, UntisUtils.ElementType.TEACHER, id);
    }

    /**
     * Get the Information about the ClassRegEvents for a specific time period and subject id (<- from https://github.com/python-webuntis/python-webuntis).
     *
     * @see Session#getClassRegEvents(LocalDate, LocalDate, UntisUtils.ElementType, Integer)
     *
     * @since 1.0
     */
    public Response getAllClassRegEventsFromSubjectId(LocalDate start, LocalDate end, int id) throws IOException {
        return this.getClassRegEvents(start, end, UntisUtils.ElementType.SUBJECT, id);
    }

    /**
     * Get the Information about the ClassRegEvents for a specific time period and room id (<- from https://github.com/python-webuntis/python-webuntis).
     *
     * @see Session#getClassRegEvents(LocalDate, LocalDate, UntisUtils.ElementType, Integer)
     *
     * @since 1.0
     */
    public Response getAllClassRegEventsFromRoomId(LocalDate start, LocalDate end, int id) throws IOException {
        return this.getClassRegEvents(start, end, UntisUtils.ElementType.ROOM, id);
    }

    /**
     * Get the Information about the ClassRegEvents for a specific time period and student id (<- from https://github.com/python-webuntis/python-webuntis).
     *
     * @see Session#getClassRegEvents(LocalDate, LocalDate, UntisUtils.ElementType, Integer)
     *
     * @since 1.0
     */
    public Response getAllClassRegEventsFromStudentId(LocalDate start, LocalDate end, int id) throws IOException {
        return this.getClassRegEvents(start, end, UntisUtils.ElementType.STUDENT, id);
    }

    /**
     * Returns all departments.
     *
     * <p>Returns {@link Departments} with all information about the departments which are registered on the given server</p>
     *
     * @return {@link Departments} with all departments
     * @throws IOException if an IO Exception occurs
     *
     * @since 1.0
     */
    public Departments getDepartments() throws IOException {
        Response response = requestManager.POST(UntisUtils.Methods.GETDEPARTMENTS.getMethod());

        JSONObject jsonResponse = response.getResponse();

        if (response.isError()) {
            throw new IOException(response.getErrorMessage());
        }
        JSONArray jsonArray = jsonResponse.getJSONArray("result");

        Departments departments = new Departments();

        for (int i = 0; i < jsonArray.length(); i++) {
            JSONObject departmentInfo = jsonArray.getJSONObject(i);
            departments.add(new Departments.DepartmentObject(departmentInfo.getString("name"),
                    departmentInfo.getInt("id"),
                    departmentInfo.getString("longName")));
        }

        return departments;
    }

    /**
     * Information about the Exams. (<- from https://github.com/python-webuntis/python-webuntis).
     *
     * <p>Requests all exams and give {@link Response} with all information about from the request back (i got an error while testing this method: no right for getExams())</p>
     *
     * @param start the beginning of the time period
     * @param end the end of the time period
     * @param id id of the exam
     * @return {@link Response} with the response from the request
     * @throws IOException if an IO Exception occurs
     *
     * @since 1.0
     */
    public Response getExams(LocalDate start, LocalDate end, int id) throws IOException {
        Map<String, String> params = UntisUtils.localDateToParams(start, end);
        params.put("examTypeId", String.valueOf(id));

        Response response = requestManager.POST(UntisUtils.Methods.GETEXAMS.getMethod(), params);

        if (response.isError()) {
            throw new IOException(response.getErrorMessage());
        }

        return response;
    }

    /**
     * Requests all exam types.
     *
     * <p>Requests all exam types and give {@link Response} with all information about from the request back (i got an error while testing this method: no right for getExamTypes())</p>
     *
     * @return {@link Response} with the response from the request
     * @throws IOException if an IO Exception occurs
     *
     * @since 1.0
     */
    public Response getExamTypes() throws IOException {
        Response response = requestManager.POST(UntisUtils.Methods.GETEXAMTYPES.getMethod());

        if (response.isError()) {
            throw new IOException(response.getErrorMessage());
        }

        return response;
    }

    /**
     * Returns all holidays registered on the given server.
     *
     * <p>Returns {@link Holidays} with all information about the holidays which are registered on the given server</p>
     *
     * @return {@link Holidays} with all information about the holidays
     * @throws IOException if an IO Exception occurs
     *
     * @since 1.0
     */
    public Holidays getHolidays() throws IOException {
        Response response = requestManager.POST(UntisUtils.Methods.GETHOLIDAYS.getMethod());

        JSONObject jsonResponse = response.getResponse();

        if (response.isError()) {
            throw new IOException(response.getErrorMessage());
        }
        JSONArray jsonArray = jsonResponse.getJSONArray("result");

        Holidays holidays = new Holidays();

        for (int i = 0; i < jsonArray.length(); i++) {
            JSONObject holidayInfo = jsonArray.getJSONObject(i);
            holidays.add(new Holidays.HolidaysObject(holidayInfo.getString("name"),
                    LocalDate.parse(String.valueOf(holidayInfo.getInt("startDate")), DateTimeFormatter.ofPattern("yyyyMMdd")),
                    LocalDate.parse(String.valueOf(holidayInfo.getInt("endDate")), DateTimeFormatter.ofPattern("yyyyMMdd")),
                    holidayInfo.getInt("id"),
                    holidayInfo.getString("longName")));
        }

        return holidays;
    }

    /**
     * Returns all klassen registered on the given server.
     *
     * <p>Returns {@link Klassen} with all information about the klasse which are registered on the given server</p>
     *
     * @see Session#getKlassen(Integer)
     *
     * @since 1.0
     */
    public Klassen getKlassen() throws IOException {
        return getKlassen(null);
    }

    /**
     * Returns all klassen from the given school year registered on the given server.
     *
     * <p>Returns {@link Klassen} with all information about the klassen from the given school year which are registered on the given server</p>
     *
     * @param schoolYearId number of the school year from which you want to get the klassen
     *
     * @return {@link Klassen} with all information about the klassen
     * @throws IOException if an IO Exception occurs
     *
     * @since 1.0
     */
    public Klassen getKlassen(Integer schoolYearId) throws IOException {
        Response response;

        if (schoolYearId != null) {
            response = requestManager.POST(UntisUtils.Methods.GETKLASSEN.getMethod(), new HashMap<String, Integer>() {{
                put("schoolyearId", schoolYearId);
            }});
        } else {
            response = requestManager.POST(UntisUtils.Methods.GETKLASSEN.getMethod());
        }

        JSONObject jsonResponse = response.getResponse();

        if (response.isError()) {
            throw new IOException(response.getErrorMessage());
        }
        JSONArray jsonArray = jsonResponse.getJSONArray("result");

        Klassen klassen = new Klassen();

        for (int i = 0; i < jsonArray.length(); i++) {
            JSONObject klassenInfo = jsonArray.getJSONObject(i);
            klassen.add(new Klassen.KlasseObject(klassenInfo.getString("name"),
                    klassenInfo.getBoolean("active"),
                    klassenInfo.getInt("id"),
                    klassenInfo.getString("longName")));
        }

        return klassen;
    }

    /**
     * Returns {@link LatestImportTime} with information about the time when the last change were made.
     *
     * <p>Returns {@link LatestImportTime} with all information about the time when the last change were made on the server</p>
     *
     * @return {@link LatestImportTime} with all information about the time when the last change were made
     * @throws IOException if an IO Exception occurs
     *
     * @since 1.0
     */
    public LatestImportTime getLatestImportTime() throws IOException {
        Response response = requestManager.POST(UntisUtils.Methods.GETHOLIDAYS.getMethod());

        JSONObject jsonResponse = response.getResponse();

        if (response.isError()) {
            throw new IOException(response.getErrorMessage());
        }

        return new LatestImportTime(jsonResponse.getInt("result"));
    }

    /**
     * Returns all rooms.
     *
     * <p>Returns {@link Rooms} with all information about the rooms which are registered on the given server</p>
     *
     * @return {@link Rooms} with all information about the rooms
     * @throws IOException if an IO Exception occurs
     *
     * @since 1.0
     */
    public Rooms getRooms() throws IOException {
        Response response = requestManager.POST(UntisUtils.Methods.GETROOMS.getMethod());

        JSONObject jsonResponse = response.getResponse();

        if (response.isError()) {
            throw new IOException(response.getErrorMessage());
        }
        JSONArray jsonArray = jsonResponse.getJSONArray("result");

        Rooms rooms = new Rooms();

        for (int i = 0; i < jsonArray.length(); i++) {
            JSONObject roomInfo = jsonArray.getJSONObject(i);
            rooms.add(new Rooms.RoomObject(roomInfo.getString("name"),
                    roomInfo.getBoolean("active"),
                    roomInfo.getInt("id"),
                    roomInfo.getString("building"),
                    roomInfo.getString("longName")));
        }

        return rooms;
    }

    /**
     * Returns all school years.
     *
     * <p>Returns {@link SchoolYears} with all information about the school years which are registered on the given server</p>
     *
     * @return {@link SchoolYears} with all information about the school years
     * @throws IOException if an IO Exception occurs
     *
     * @since 1.0
     */
    public SchoolYears getSchoolYears() throws IOException {
        Response response = requestManager.POST(UntisUtils.Methods.GETSCHOOLYEARS.getMethod());

        JSONObject jsonResponse = response.getResponse();

        if (response.isError()) {
            throw new IOException(response.getErrorMessage());
        }
        JSONArray jsonArray = jsonResponse.getJSONArray("result");

        SchoolYears schoolYears = new SchoolYears();

        for (int i = 0; i < jsonArray.length(); i++) {
            JSONObject schoolYearInfo = jsonArray.getJSONObject(i);
            schoolYears.add(new SchoolYears.SchoolYearObject(schoolYearInfo.getString("name"),
                    LocalDate.parse(String.valueOf(schoolYearInfo.getInt("startDate")), DateTimeFormatter.ofPattern("yyyyMMdd")),
                    LocalDate.parse(String.valueOf(schoolYearInfo.getInt("endDate")), DateTimeFormatter.ofPattern("yyyyMMdd")),
                    schoolYearInfo.getInt("id")));
        }

        return schoolYears;
    }

    /**
     * Requests all status data.
     *
     * <p>Requests all status data and give {@link Response} with all information about from the request back</p>
     *
     * @return {@link Response} with the response from the request
     * @throws IOException if an IO Exception occurs
     *
     * @since 1.0
     */
    public Response getStatusData() throws IOException {
        Response response = requestManager.POST(UntisUtils.Methods.GETSTATUSDATA.getMethod());

        if (response.isError()) {
            throw new IOException(response.getErrorMessage());
        }
        return response;
    }

    /**
     * Returns all subjects.
     *
     * <p>Returns {@link Subjects} with all information about the subjects which are registered on the given server</p>
     *
     * @return {@link Subjects} with all information about the subjects
     * @throws IOException if an IO Exception occurs
     *
     * @since 1.0
     */
    public Subjects getSubjects() throws IOException {
        Response response = requestManager.POST(UntisUtils.Methods.GETSUBJECTS.getMethod());

        JSONObject jsonResponse = response.getResponse();

        if (response.isError()) {
            throw new IOException(response.getErrorMessage());
        }
        JSONArray jsonArray = jsonResponse.getJSONArray("result");

        Subjects subjects = new Subjects();

        for (int i = 0; i < jsonArray.length(); i++) {
            JSONObject subjectInfo = jsonArray.getJSONObject(i);
            subjects.add(new Subjects.SubjectObject(subjectInfo.getString("name"),
                    subjectInfo.getBoolean("active"),
                    subjectInfo.getInt("id"),
                    subjectInfo.getString("alternateName"),
                    subjectInfo.getString("backColor"),
                    subjectInfo.getString("foreColor"),
                    subjectInfo.getString("longName")));
        }

        return subjects;
    }

    /**
     * Returns all registered teachers on the given server.
     *
     * <p>Returns {@link Teachers} with all information about the teachers which are registered on the given server</p>
     *
     * @return {@link Teachers} with all information about the teachers
     * @throws IOException if an IO Exception occurs
     *
     * @since 1.0
     */
    public Teachers getTeachers() throws IOException {
        Response response = requestManager.POST(UntisUtils.Methods.GETTEACHERS.getMethod());

        JSONObject jsonResponse = response.getResponse();

        if (response.isError()) {
            throw new IOException(response.getErrorMessage());
        }
        JSONArray jsonArray = jsonResponse.getJSONArray("result");

        Teachers teachers = new Teachers();

        for (int i = 0; i < jsonArray.length(); i++) {
            JSONObject teacherInfo = jsonArray.getJSONObject(i);
            teachers.add(new Teachers.TeacherObject(teacherInfo.getString("name"),
                    teacherInfo.getBoolean("active"),
                    teacherInfo.getInt("id"),
                    teacherInfo.getString("title"),
                    teacherInfo.getString("foreName"),
                    teacherInfo.getString("longName")));
        }

        return teachers;
    }

    /**
     * Returns all registered timegrid units on the given server.
     *
     * <p>Returns {@link TimegridUnits} with all information about the timegrid units which are registered on the given server</p>
     *
     * @return {@link TimegridUnits} with all information about the timegrid units
     * @throws IOException if an IO Exception occurs
     *
     * @since 1.0
     */
    public TimegridUnits getTimegridUnits() throws IOException {
        Response response = requestManager.POST(UntisUtils.Methods.GETTIMEGRIDUNTIS.getMethod());

        JSONObject jsonResponse = response.getResponse();

        if (response.isError()) {
            throw new IOException(response.getErrorMessage());
        }
        JSONArray jsonArray = jsonResponse.getJSONArray("result");

        TimegridUnits timegridUnits = new TimegridUnits();

        for (int i = 0; i < jsonArray.length(); i++) {
            JSONObject timegridUnitInfo = jsonArray.getJSONObject(i);

            TimeUnits timeUnits = new TimeUnits();

            JSONArray arrayJsonArray = timegridUnitInfo.getJSONArray("timeUnitObjects");
            for (int j = 0; j < arrayJsonArray.length(); j++) {
                JSONObject timegridUnitInfoObject = arrayJsonArray.getJSONObject(j);

                LocalTime startTime;
                LocalTime endTime;

                try {
                    startTime = LocalTime.parse(String.valueOf(timegridUnitInfoObject.getInt("startTime")), DateTimeFormatter.ofPattern("HHmm"));
                } catch (DateTimeParseException e) {
                    startTime = LocalTime.parse(String.valueOf(timegridUnitInfoObject.getInt("startTime")), DateTimeFormatter.ofPattern("Hmm"));
                }

                try {
                    endTime = LocalTime.parse(String.valueOf(timegridUnitInfoObject.getInt("endTime")), DateTimeFormatter.ofPattern("HHmm"));
                } catch (DateTimeParseException e) {
                    endTime = LocalTime.parse(String.valueOf(timegridUnitInfoObject.getInt("endTime")), DateTimeFormatter.ofPattern("Hmm"));
                }

                timeUnits.add(new TimeUnits.TimeUnitObject(timegridUnitInfoObject.getString("name"),
                        startTime,
                        endTime));
            }

            timegridUnits.add(new TimegridUnits.TimegridUnitObject(timegridUnitInfo.getInt("day"),
                    timeUnits));
        }

        return timegridUnits;
    }

    /**
     * Returns information about the current school year.
     *
     * <p>Returns {@link SchoolYears.SchoolYearObject} with all information about the current school year</p>
     *
     * @return {@link SchoolYears.SchoolYearObject} with all information about the current school year
     * @throws IOException if an IO Exception occurs
     *
     * @since 1.0
     */
    public SchoolYears.SchoolYearObject getCurrentSchoolYear() throws IOException {
        Response response = requestManager.POST(UntisUtils.Methods.GETCURRENTSCHOOLYEAR.getMethod());

        JSONObject jsonResponse = response.getResponse();

        if (response.isError()) {
            throw new IOException(response.getErrorMessage());
        }
        JSONObject jsonObject = jsonResponse.getJSONObject("result");

        return new SchoolYears.SchoolYearObject(jsonObject.getString("name"),
                LocalDate.parse(String.valueOf(jsonObject.getInt("startDate")), DateTimeFormatter.ofPattern("yyyyMMdd")),
                LocalDate.parse(String.valueOf(jsonObject.getInt("endDate")), DateTimeFormatter.ofPattern("yyyyMMdd")),
                jsonObject.getInt("id"));
    }

    /**
     * Returns the lessons / timetable for a specific time period.
     *
     * <p> Returns the lessons / timetable for a specific time period and klasse / teacher / subject / room / student id<p/>
     *
     * @param start the beginning of the time period
     * @param end the end of the time period
     * @param elementType type on which the timetable should be oriented
     * @param id id of the {@code elementType}
     * @return {@link Timetable} with all information about the lessons
     * @throws IOException if an IO Exception occurs
     *
     * @since 1.0
     */
    public Timetable getTimetable(LocalDate start, LocalDate end, UntisUtils.ElementType elementType, int id) throws IOException {
        HashMap<String, String> params = UntisUtils.localDateToParams(start, end);

        params.put("type", String.valueOf(elementType.getElementType()));
        params.put("id", String.valueOf(id));

        Response response = requestManager.POST("getTimetable", params);

        JSONObject jsonResponse = response.getResponse();

        if (response.isError()) {
            throw new IOException(response.getErrorMessage());
        }
        JSONArray jsonArray = jsonResponse.getJSONArray("result");

        Timetable timetable = new Timetable();

        for (int i = 0; i < jsonArray.length(); i++) {
            JSONObject timetableInfos = jsonArray.getJSONObject(i);

            String[] arrayKeys = {"kl", "te", "su", "ro"};
            HashMap<Integer, HashSet<Integer>> arrayValues = new HashMap<>();

            for (int j = 0; j < arrayKeys.length; j++) {
                JSONArray arrayJSONArray = timetableInfos.getJSONArray(arrayKeys[j]);
                HashSet<Integer> values = new HashSet<>();
                for (int k = 0; k < arrayJSONArray.length(); k++) {
                    values.add(arrayJSONArray.getJSONObject(k).getInt("id"));
                }
                arrayValues.put(j, values);
            }


            LocalTime startTime;
            LocalTime endTime;

            try {
                startTime = LocalTime.parse(String.valueOf(timetableInfos.getInt("startTime")), DateTimeFormatter.ofPattern("HHmm"));
            } catch (DateTimeParseException e) {
                startTime = LocalTime.parse(String.valueOf(timetableInfos.getInt("startTime")), DateTimeFormatter.ofPattern("Hmm"));
            }

            try {
                endTime = LocalTime.parse(String.valueOf(timetableInfos.getInt("endTime")), DateTimeFormatter.ofPattern("HHmm"));
            } catch (DateTimeParseException e) {
                endTime = LocalTime.parse(String.valueOf(timetableInfos.getInt("endTime")), DateTimeFormatter.ofPattern("Hmm"));
            }

            UntisUtils.LessonCode code = null;
            if (timetableInfos.has("code")) {
                code = UntisUtils.LessonCode.valueOf(timetableInfos.getString("code").toUpperCase());
            }

            timetable.add(new Timetable.Lesson(LocalDate.parse(String.valueOf(timetableInfos.getInt("date")), DateTimeFormatter.ofPattern("yyyyMMdd")),
                    startTime,
                    endTime,
                    arrayValues.get(0),
                    arrayValues.get(1),
                    arrayValues.get(3),
                    arrayValues.get(2),
                    code,
                    timetableInfos.getString("activityType")));
        }

        return timetable;
    }

    /**
     * Returns the lessons / timetable for a specific time period and klasse id.
     *
     * @see Session#getTimetable(LocalDate, LocalDate, UntisUtils.ElementType, int)
     *
     * @since 1.0
     */
    public Timetable getTimetableFromKlasseId(LocalDate start, LocalDate end, int klasseId) throws IOException {
        return this.getTimetable(start, end, UntisUtils.ElementType.KLASSE, klasseId);
    }

    /**
     * Returns the lessons / timetable for a specific time period and teacher id.
     *
     * @see Session#getTimetable(LocalDate, LocalDate, UntisUtils.ElementType, int)
     *
     * @since 1.0
     */
    public Timetable getTimetableFromTeacherId(LocalDate start, LocalDate end, int teacherId) throws IOException {
        return this.getTimetable(start, end, UntisUtils.ElementType.TEACHER, teacherId);
    }

    /**
     * Returns the lessons / timetable for a specific time period and subject id.
     *
     * @see Session#getTimetable(LocalDate, LocalDate, UntisUtils.ElementType, int)
     *
     * @since 1.0
     */
    public Timetable getTimetableFromSubjectId(LocalDate start, LocalDate end, int subjectId) throws IOException {
        return this.getTimetable(start, end, UntisUtils.ElementType.SUBJECT, subjectId);
    }

    /**
     * Returns the lessons / timetable for a specific time period and room id.
     *
     * @see Session#getTimetable(LocalDate, LocalDate, UntisUtils.ElementType, int)
     *
     * @since 1.0
     */
    public Timetable getTimetableFromRoomId(LocalDate start, LocalDate end, int roomId) throws IOException {
        return this.getTimetable(start, end, UntisUtils.ElementType.ROOM, roomId);
    }

    /**
     * Returns the lessons / timetable for a specific time period and student id.
     *
     * @see Session#getTimetable(LocalDate, LocalDate, UntisUtils.ElementType, int)
     *
     * @since 1.0
     */
    public Timetable getTimetableFromStudentId(LocalDate start, LocalDate end, int studentId) throws IOException {
        return this.getTimetable(start, end, UntisUtils.ElementType.STUDENT, studentId);
    }

    /**
     * Requests timetable with absence for a specific time period
     *
     * <p>Requests timetable with absence for a specific time period and give {@link Response} with all information about from the request back (i got an error while testing this method: no right for getTimetableWithAbsences())</p>
     *
     * @param start the beginning of the time period
     * @param end the end of the time period
     * @return {@link Response} with the response from the request
     * @throws IOException if an IO Exception occurs
     *
     * @since 1.0
     */
    public Response getTimetableWithAbsence(LocalDate start, LocalDate end) throws IOException {
        Response response = requestManager.POST(UntisUtils.Methods.GETTIMETABLEWITHABSENCE.getMethod(), new HashMap<String, Object>() {{
            put("options", UntisUtils.localDateToParams(start, end));
        }});

        if (response.isError()) {
            throw new IOException(response.getErrorMessage());
        }

        return response;
    }

    /**
     * Allows to request custom data.
     *
     * @see Session#getCustomData(String, Map)
     *
     * @since 1.0
     */
    public Response getCustomData(String method) throws IOException {
        return this.getCustomData(method, null);
    }

    /**
     * Allows to request custom data.
     *
     * <p>Requests custom data and give {@link Response} with all information about from the request back</p>
     *
     * @return {@link Response} with the response from the request
     * @throws IOException if an IO Exception occurs
     *
     * @since 1.0
     */
    public Response getCustomData(String method, Map<String, ?> params) throws IOException {
        if (params != null) {
            return requestManager.POST(method, params);
        } else {
            return requestManager.POST(method);
        }
    }

    /**
     * Logs out from the server.
     *
     * <p>Send an logout request to the server. Throws {@link IOException} if an IO Exception occurs</p>
     *
     * @throws IOException if an IO Exception occurs
     *
     * @since 1.0
     */
    public void logout() throws IOException {
        requestManager.POST(UntisUtils.Methods.LOGOUT.getMethod());
    }

    /**
     * Refreshes the session.
     *
     * <p>Logs out and then logs in again. If this works the {@link RequestManager} gets refreshed.
     * Throws {@link IOException} if an IO Exception occurs or {@link LoginException} (which extends from IOException) if login fails</p>
     *
     * @throws IOException if an IO Exception occurs
     *
     * @since 1.0
     */
    public void refresh() throws IOException {
        logout();

        RequestManager requestManager = new RequestManager(infos);

        HashMap<String, Object> params = new HashMap<>();
        params.put("user", infos.getUsername());
        params.put("password", infos.getPassword());
        params.put("client", "");

        if (requestManager.POST(UntisUtils.Methods.LOGIN.getMethod(), params).isError()) {
            throw new LoginException("Failed to login");
        } else {
            this.requestManager = requestManager;
        }
    }

    /**
     * Logs in to the server.
     *
     * <p>Send an login request to the server and returns {@link Session} if the login was successful.
     * Throws {@link IOException} if an IO Exception occurs or {@link LoginException} (which inherits from IOException) if login fails</p>
     *
     * @see Session#login(String, String, String, String, String)
     */
    public static Session login(String username, String password, String server, String schoolName) throws IOException {
        return login(username, password, server, schoolName, "");
    }

    /**
     * Logs in to the server.
     *
     * <p>Send an login request to the server and returns {@link Session} if the login was successful.
     * Throws {@link IOException} if an IO Exception occurs or {@link LoginException} (which inherits from IOException) if login fails</p>
     *
     * @param server the server from your school as URL
     * @param schoolName school name of the school you want to connect to
     * @param username the username used for the API
     * @param password the password used for the API
     * @param userAgent the user agent you want to send with
     * @return a {@link Session} session
     * @throws IOException if an IO Exception occurs
     *
     * @since 1.0
     */
    public static Session login(String username, String password, String server, String schoolName, String userAgent) throws IOException {
        Infos infos = new Infos(username, password, server, schoolName, userAgent);

        RequestManager requestManager = new RequestManager(infos);

        HashMap<String, String> params = new HashMap<>();
        params.put("user", infos.getUsername());
        params.put("password", infos.getPassword());
        params.put("client", userAgent);

        if (requestManager.POST(UntisUtils.Methods.LOGIN.getMethod(), params).isError()) {
            throw new LoginException("Failed to login");
        } else {
            return new Session(infos, requestManager);
        }
    }

}
