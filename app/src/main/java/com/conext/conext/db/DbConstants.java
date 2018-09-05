package com.conext.conext.db;

/**
 * Created by Ashith VL on 7/23/2017.
 */

public class DbConstants {

    public static final String TABLE_NAME_USER_PROFILE = "USER_PROFILE";
    public static final String TABLE_NAME_TAG_MASTER = "TAG_MASTER";
    public static final String TABLE_NAME_EDUCATION_MASTER = "EDUCATION_MASTER";
    public static final String TABLE_NAME_WORK_MASTER = "WORK_MASTER";
    public static final String TABLE_NAME_LOCATION = "LOCATION";
    public static final String TABLE_NAME_EVENT_TYPE_MASTER = "EVENT_TYPE_MASTER";
    public static final String TABLE_NAME_USER_TAG = "USER_TAG";
    public static final String TABLE_NAME_USER_EDUCATION = "USER_EDUCATION";
    public static final String TABLE_QUERY_USER_PARTICIPANTS = "USER_PARTICIPANTS";
    public static final String TABLE_NAME_USER_WORK = "USER_WORK";
    public static final String TABLE_NAME_USER_EVENT = "USER_EVENT";

    public static final String CREATE_TABLE_QUERY_USER_PROFILE =
            "CREATE TABLE USER_PROFILE (\n" +
                    "\tUserKey INTEGER PRIMARY KEY NOT NULL DEFAULT '10000000' ,\n" +
                    "\tEmailID varchar(256) NOT NULL ,\n" +
                    "\tPassword varchar(256) NOT NULL ,\n" +
                    "\tFirstName varchar(256) NOT NULL ,\n" +
                    "\tLastName varchar(256) NOT NULL ,\n" +
                    "\tTitleDisplay varchar(256) NOT NULL,\n" +
                    "\tCompanyDisplay varchar(256) NOT NULL,\n" +
                    "\tProfilePic BLOB NOT NULL,\n" +
                    "\tDeleteFlag BOOLEAN NOT NULL,\n" +
                    "\tCreatedBy varchar(256) NOT NULL,\n" +
                    "\tCreatedTs TIMESTAMP NOT NULL,\n" +
                    "\tModifiedBy varchar(256) NOT NULL,\n" +
                    "\tModifiedTs TIMESTAMP NOT NULL,\n" +
                    "\tLocationKey INT(10) NOT NULL,\n" +
                    "\tFOREIGN KEY(LocationKey) REFERENCES LOCATION(LocationKey)\n" +
                    ")";

    public static final String CREATE_TABLE_QUERY_TAG_MASTER =
            "CREATE TABLE TAG_MASTER (\n" +
                    "\tTagKey INTEGER PRIMARY KEY NOT NULL DEFAULT '10000000',\n" +
                    "\tTagName varchar(256) NOT NULL,\n" +
                    "\tTagDescription varchar(256),\n" +
                    "\tCreatedBy varchar(256) NOT NULL,\n" +
                    "\tCreatedTs TIMESTAMP NOT NULL,\n" +
                    "\tModifiedBy varchar(256) NOT NULL,\n" +
                    "\tModifiedTs TIMESTAMP NOT NULL\n" +
                    ")";

    public static final String CREATE_TABLE_QUERY_EDUCATION_MASTER =
            "CREATE TABLE EDUCATION_MASTER (\n" +
                    "\tEducationKey INTEGER PRIMARY KEY NOT NULL DEFAULT '100000',\n" +
                    "\tSchoolName varchar(256) NOT NULL,\n" +
                    "\tCity varchar(256) NOT NULL,\n" +
                    "\tAddress varchar(256) NOT NULL,\n" +
                    "\tType varchar(256) NOT NULL,\n" +
                    "\tInfo varchar(256) NOT NULL,\n" +
                    "\tWebsite varchar(256) NOT NULL,\n" +
                    "\tDeleteFlag BOOLEAN NOT NULL,\n" +
                    "\tCreatedBy varchar(256) NOT NULL,\n" +
                    "\tCreatedTs TIMESTAMP NOT NULL,\n" +
                    "\tModifiedBy varchar(256) NOT NULL,\n" +
                    "\tModifiedTs TIMESTAMP NOT NULL,\n" +
                    "\tLocationKey INT(10) NOT NULL,\n" +
                    "\tFOREIGN KEY(LocationKey) REFERENCES LOCATION(LocationKey)\n" +
                    ")";

    public static final String CREATE_TABLE_QUERY_WORK_MASTER =
            "CREATE TABLE WORK_MASTER (\n" +
                    "\tWorkKey INTEGER PRIMARY KEY NOT NULL DEFAULT '1000000',\n" +
                    "\tLinkedInId INT(8) NOT NULL DEFAULT '1000000' ,\n" +
                    "\tName varchar(256) NOT NULL,\n" +
                    "\tInfo varchar(256) NOT NULL,\n" +
                    "\tSpecialities varchar(256) NOT NULL,\n" +
                    "\tWebsite varchar(256) NOT NULL,\n" +
                    "\tIndustry varchar(256) NOT NULL,\n" +
                    "\tType varchar(256) NOT NULL,\n" +
                    "\tCompanySize INT(10),\n" +
                    "\tFounded INT(4),\n" +
                    "\tDeleteFlag BOOLEAN NOT NULL,\n" +
                    "\tCreatedBy varchar(256) NOT NULL,\n" +
                    "\tCreatedTs TIMESTAMP NOT NULL,\n" +
                    "\tModifiedTs TIMESTAMP NOT NULL,\n" +
                    "\tModifiedBy varchar(256) NOT NULL,\n" +
                    "\tLocationKey INT(10) NOT NULL,\n" +
                    "\tFOREIGN KEY(LocationKey) REFERENCES LOCATION(LocationKey)\n" +
                    ")";

    public static final String CREATE_TABLE_QUERY_LOCATION =
            "CREATE TABLE LOCATION (\n" +
                    "\tLocationKey INTEGER PRIMARY KEY NOT NULL DEFAULT '1000000000',\n" +
                    "\tType varchar(10) NOT NULL,\n" +
                    "\tLatitude varchar(20) NOT NULL,\n" +
                    "\tLongitude varchar(20) NOT NULL,\n" +
                    "\tDeleteFlag BOOLEAN NOT NULL,\n" +
                    "\tCreatedBy varchar(256) NOT NULL,\n" +
                    "\tCreatedTs TIMESTAMP NOT NULL,\n" +
                    "\tModifiedBy varchar(256) NOT NULL,\n" +
                    "\tModifiedTs TIMESTAMP NOT NULL\n" +
                    ")";

    public static final String CREATE_TABLE_QUERY_EVENT_TYPE_MASTER =
            "CREATE TABLE EVENT_TYPE_MASTER (\n" +
                    "\tEventTypeKey INTEGER PRIMARY KEY NOT NULL DEFAULT '1000000000',\n" +
                    "\tName varchar(256) NOT NULL,\n" +
                    "\tDefaultOC varchar(256) NOT NULL,\n" +
                    "\tDefaultImage varchar(256) NOT NULL,\n" +
                    "\tDeleteFlag BOOLEAN NOT NULL,\n" +
                    "\tCreatedBy varchar(256) NOT NULL,\n" +
                    "\tCreatedTs TIMESTAMP NOT NULL,\n" +
                    "\tModifiedBy varchar(256) NOT NULL,\n" +
                    "\tModifiedTs TIMESTAMP NOT NULL\n" +
                    ")";

    public static final String CREATE_TABLE_QUERY_USER_TAG =
            "CREATE TABLE USER_TAG (\n" +
                    "\tUserKey INTEGER NOT NULL,\n" +
                    "\tTagKey INTEGER NOT NULL,\n" +
                    "\tMentorFlag BOOLEAN NOT NULL,\n" +
                    "\tMenteeFlag BOOLEAN NOT NULL,\n" +
                    "\tDeleteFlag BOOLEAN NOT NULL,\n" +
                    "\tCreatedBy varchar(256) NOT NULL,\n" +
                    "\tCreatedTs TIMESTAMP NOT NULL,\n" +
                    "\tModifiedBy varchar(256) NOT NULL,\n" +
                    "\tModifiedTs TIMESTAMP NOT NULL,\n" +
                    "\tPRIMARY KEY (UserKey, TagKey),\n" +
                    "\tFOREIGN KEY(TagKey) REFERENCES TAG_MASTER(TagKey),\n" +
                    "\tFOREIGN KEY(UserKey) REFERENCES USER_PROFILE(UserKey)\n" +
                    ")";

    public static final String CREATE_TABLE_QUERY_USER_PARTICIPANTS =
            "CREATE TABLE USER_PARTICIPANTS (\n" +
                    "\tUserKey INTEGER NOT NULL DEFAULT 0,\n" +
                    "\tEventKey INTEGER NOT NULL DEFAULT 0,\n" +
                    "\tMentorFlag BOOLEAN NOT NULL DEFAULT 0,\n" +
                    "\tMenteeFlag BOOLEAN NOT NULL DEFAULT 0,\n" +
                    "\tParticipantStatus varchar(256) NOT NULL DEFAULT 3,\n" +
                    "\tFOREIGN KEY(EventKey) REFERENCES USER_EVENT(EventKey),\n" +
                    "\tFOREIGN KEY(UserKey) REFERENCES USER_PROFILE(UserKey)\n" +
                    ")";

/*    ParticipantStatus : 0 --> yes ,
                        1 --> intrested ,
                        2 --> no,
                        3 --> informed*/

    public static final String CREATE_TABLE_QUERY_USER_EDUCATION =
            "CREATE TABLE USER_EDUCATION (\n" +
                    "\tEducationKey INTEGER NOT NULL,\n" +
                    "\tUserKey INT NOT NULL,\n" +
                    "\tYearStart INT NOT NULL,\n" +
                    "\tYearFinish INT NOT NULL,\n" +
                    "\tDegree varchar(10) NOT NULL,\n" +
                    "\tFieldOfStudy varchar(256) NOT NULL,\n" +
                    "\tGrade varchar(10) NOT NULL,\n" +
                    "\tActivities varchar(256) NOT NULL,\n" +
                    "\tDescription varchar(256) NOT NULL,\n" +
                    "\tDeleteFlag BOOLEAN NOT NULL DEFAULT 0,\n" +
                    "\tCreatedBy varchar(256) NOT NULL,\n" +
                    "\tCreatedTs TIMESTAMP NOT NULL,\n" +
                    "\tModifiedBy varchar(256) NOT NULL,\n" +
                    "\tModifiedTs TIMESTAMP NOT NULL,\n" +
                    "\tPRIMARY KEY (UserKey, EducationKey),\n" +
                    "\tFOREIGN KEY(EducationKey) REFERENCES EDUCATION_MASTER(EducationKey),\n" +
                    "\tFOREIGN KEY(UserKey) REFERENCES USER_PROFILE(UserKey)\n" +
                    ")";

    public static final String CREATE_TABLE_QUERY_USER_WORK =
            "CREATE TABLE USER_WORK (\n" +
                    "\tUserKey INTEGER NOT NULL,\n" +
                    "\tWorkKey INTEGER NOT NULL,\n" +
                    "\tWorkTitle varchar(25) NOT NULL,\n" +
                    "\tLocationKey INT NOT NULL,\n" +
                    "\tYearStart INT NOT NULL,\n" +
                    "\tYearFinish INT NOT NULL,\n" +
                    "\tDescription varchar(256) NOT NULL,\n" +
                    "\tDeleteFlag BOOLEAN NOT NULL DEFAULT 0,\n" +
                    "\tCreatedBy varchar(256) NOT NULL,\n" +
                    "\tCreatedTs TIMESTAMP NOT NULL,\n" +
                    "\tModifiedBy varchar(256) NOT NULL,\n" +
                    "\tModifiedTs TIMESTAMP NOT NULL,\n" +
                    "\tPRIMARY KEY (UserKey, WorkKey),\n" +
                    "\tFOREIGN KEY(LocationKey) REFERENCES LOCATION(LocationKey),\n" +
                    "\tFOREIGN KEY(WorkKey) REFERENCES WORK_MASTER(WorkKey),\n" +
                    "\tFOREIGN KEY(UserKey) REFERENCES USER_PROFILE(UserKey)\n" +
                    ")";

    public static final String CREATE_TABLE_QUERY_USER_EVENT =
            "CREATE TABLE USER_EVENT (\n" +
                    "\tEventKey INTEGER PRIMARY KEY NOT NULL DEFAULT 'null',\n" +
                    "\tEventTypeKey INT NOT NULL,\n" +
                    "\tTagKey INT(8) NOT NULL,\n" +
                    "\tLocationKey INT(8) NOT NULL,\n" +
                    "\tDescription varchar(256) NOT NULL,\n" +
                    "\tEventStartTs TIMESTAMP NOT NULL,\n" +
                    "\tEventEndTs TIMESTAMP NOT NULL,\n" +
                    "\tAddress varchar(256) NOT NULL,\n" +
                    "\tisOpen BOOLEAN NOT NULL DEFAULT 0,\n" +
                    "\tImageKey BLOB NOT NULL,\n" +
                    "\tDeleteFlag BOOLEAN NOT NULL DEFAULT 0,\n" +
                    "\tCreatedBy varchar(256) NOT NULL,\n" +
                    "\tCreatedTs TIMESTAMP NOT NULL,\n" +
                    "\tModifiedBy varchar(256) NOT NULL,\n" +
                    "\tModifiedTs TIMESTAMP NOT NULL,\n" +
                    "\tEventTitle varchar(256) NOT NULL,\n" +
                    "\tUserKey INTEGER NOT NULL,\n" +
                    "\tFOREIGN KEY(UserKey) REFERENCES USER_PROFILE(UserKey),\n" +
                    "\tFOREIGN KEY(EventTypeKey) REFERENCES EVENT_TYPE_MASTER(EventTypeKey),\n" +
                    "\tFOREIGN KEY(TagKey) REFERENCES TAG_MASTER(TagKey),\n" +
                    "\tFOREIGN KEY(LocationKey) REFERENCES LOCATION(LocationKey )\n" +
                    ")";


    public static final String USER_KEY = "UserKey";
    public static final String EVENT_TYPE_KEY = "EventTypeKey";
    public static final String TAG_KEY = "TagKey";
    public static final String LOCATION_KEY = "LocationKey";
    public static final String DESCRIPTION = "Description";
    public static final String EVENT_TITLE = "EventTitle";
    public static final String EVENT_START_TIMESTAMP = "EventStartTs";
    public static final String EVENT_END_TIMESTAMP = "EventEndTs";
    public static final String TAG_NAME = "TagName";
    public static final String TAG_DESCRIPTION = "TagDescription";
    public static final String CREATED_BY = "CreatedBy";
    public static final String CREATED_TIMESTAMP = "CreatedTs";
    public static final String MODIFIED_BY = "ModifiedBy";
    public static final String MODIFIED_TIMESTAMP = "ModifiedTs";
    public static final String SCHOOL_NAME = "SchoolName";
    public static final String CITY = "City";
    public static final String ADDRESS = "Address";
    public static final String TYPE = "Type";
    public static final String INFO = "Info";
    public static final String WEBSITE = "Website";
    public static final String DELETE_FLAG = "DeleteFlag";
    public static final String LINKED_IN_ID = "LinkedInId";
    public static final String NAME = "Name";
    public static final String SPECIALITIES = "Specialities";
    public static final String INDUSTRY = "Industry";
    public static final String COMPANY_SIZE = "CompanySize";
    public static final String FOUNDED = "Founded";
    public static final String DEFAULT_OC = "DefaultOC";
    public static final String DEFAULT_IMAGE = "DefaultImage";
    public static final String EMAIL_ID = "EmailID";
    public static final String PASSWORD = "Password";
    public static final String IS_OPEN = "isOpen";
    public static final String IMAGE_KEY = "ImageKey";
    public static final String FIRST_NAME = "FirstName";
    public static final String LAST_NAME = "LastName";
    public static final String TITLE_DISPLAY = "TitleDisplay";
    public static final String COMPANY_DISPLAY = "CompanyDisplay";
    public static final String PROFILE_PIC = "ProfilePic";
    public static final String LATITUDE = "Latitude";
    public static final String LONGITUDE = "Longitude";
    public static final String MENTOR_FLAG = "MentorFlag";
    public static final String MENTEE_FLAG = "MenteeFlag";
    public static final String EDUCATION_KEY = "EducationKey";
    public static final String YEAR_START = "YearStart";
    public static final String YEAR_FINISH = "YearFinish";
    public static final String DEGREE = "Degree";
    public static final String FIELD_OF_STUDY = "FieldOfStudy";
    public static final String GRADE = "Grade";
    public static final String ACTIVITIES = "Activities";
    public static final String EVENT_KEY = "EventKey";
    public static final String PARTICIPANT_STATUS = "ParticipantStatus";
    public static final String MENTEE = "Mentee";
    public static final String MENTOR = "Mentor";
    public static final String PARTICIPANT = "Participant";
    public static final String JAVA = "Java";
    public static final String C = "C";
    public static final String C_PLUS_PLUS = "C++";
    public static final String PYTHON = "Python";
    public static final String DOT_NET = ".NET";
    public static final String PHOTOGRAPHY = "Photography";
    public static final String SKILING = "Skiling";
    public static final String FITNESS = "Fitness";
    public static final String HIKING = "Hiking";
    public static final String SOCCER = "Soccer";
    public static final String ANALYTICS = "Analytics";
    public static final String JSP = "Jsp";
    public static final String JAVA_SCRIPT = "JavaScript";
    public static final String GHS_SCHOOL = "GHS School";
    public static final String MENTORSHIP = "Mentorship";
    public static final String ALUMNI_MEET = "Alumni Meet";
    public static final String LUNCH_MEETUP = "Lunch Meetup";
    public static final String COFFEE_CONNECT = "Coffee Connect";
    public static final String FUN = "Fun";


    public static final String GET_EVENT_TYPE = "SELECT * FROM " + TABLE_NAME_EVENT_TYPE_MASTER;

}
