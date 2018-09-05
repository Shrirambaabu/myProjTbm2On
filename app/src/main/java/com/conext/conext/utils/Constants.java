package com.conext.conext.utils;

/**
 * Created by sunil on 22-03-2016.
 */
public class Constants {
    //This is the public api key of our application
    //public static final String APP_CLIENT_ID = "7707xgx0frki24";
    public static final String APP_CLIENT_ID = "81fkih226tjoji";

    //This is the private api key of our application
    //public static final String APP_CLIENT_SECRET_KEY = "ACYmPeLLHfLBSlVR";
    public static final String APP_CLIENT_SECRET_KEY = "iW6V3lDBYsrVyjPt";

    //This is any string we want to use. This will be used for avoid CSRF attacks.
    //You can generate one here: http://strongpasswordgenerator.com/
    public static final String STATE = "E3ZYKC1T6H2yP4z";

    //This is the url that LinkedIn Auth process will redirect to. We can put whatever we want that starts with http:// or https:// .
    //We use a made up url that we will intercept when redirecting. Avoid Uppercases.
    public static final String REDIRECT_URI = "http://www.con.com";

    public static final String SCOPES = "r_basicprofile";

    //These are constants used for build the urls
    public static final String AUTHORIZATION_URL = "https://www.linkedin.com/uas/oauth2/authorization";
    public static final String ACCESS_TOKEN_URL = "https://www.linkedin.com";
    public static final String SECRET_KEY_PARAM = "client_secret";
    public static final String RESPONSE_TYPE_PARAM = "response_type";
    public static final String GRANT_TYPE_PARAM = "grant_type";
    public static final String GRANT_TYPE = "authorization_code";
    public static final String RESPONSE_TYPE_VALUE = "code";
    public static final String CLIENT_ID_PARAM = "client_id";
    public static final String SCOPE_PARAM = "scope";
    public static final String STATE_PARAM = "state";
    public static final String REDIRECT_URI_PARAM = "redirect_uri";
    /*---------------------------------------*/

    public static final String QUESTION_MARK = "?";
    public static final String AMPERSAND = "&";
    public static final String EQUALS = "=";

    //BASE URL for LinkedIn API
    public static final String LINKEDIN_API_BASE_URL = "https://api.linkedin.com";

    //Request code for Activity callback
    public static final int LINKEDIN_LOGIN_REQ_CODE = 1;

    //Max skills
    public static final int MAX_SKILLS_ALLOWED = 5;

   /* public static String BaseIP = "http://192.168.0.105:8080";
    public static String BaseUri = BaseIP + "/conext/webapi/conext";*/

    public static String BaseIP = "http://servicetier.jvmhost.net";
    public static String BaseUri = BaseIP + "/webapi/conext";

    public static final String LOGIN_URL = BaseUri + "/login";
    public static final String UPDATE_PROFILE_PIC = BaseUri + "/updateProfilePicture";
    public static final String FACEBOOK_LIKES = BaseUri + "/facebookLikes";
    public static final String UPDATE_EVENT_PARTICIPANT = BaseUri + "/updateEventDetailsParticipant";
    public static final String TAG_KEY_URL = BaseUri + "/getTagKey";
    public static final String GET_TAG_USER_KEY_URL = BaseUri + "/getTagUserKey";
    public static final String GET_EVENT_TYPE_KEY_URL = BaseUri + "/getEventType";
    public static final String GET_EVENT_SELETCTED_URL = BaseUri + "/getEventTypeKey";
    public static final String ALUMNI_URL = BaseUri + "/getAluminiDetails";
    public static final String GET_MY_EVENTS_ATTENDED_URL = BaseUri + "/getMyAttendedEvents";
    public static final String GET_MY_PARTICULAR_EVENTS_ATTENDED_URL = BaseUri + "/getParticularMyAttendedEvents";
    public static final String GET_USER_MENTOR_SKILL_URL = BaseUri + "/getUserMentorSkill";
    public static final String GET_PROFILE_SKILL_URL = BaseUri + "/getProfileSkills";
    public static final String GET_USER_MENTEE_SKILL_URL = BaseUri + "/getUserMenteeSkill";
    public static final String GET_MENTEE_MENTOR_REQUEST_URL = BaseUri + "/getMenteeMentorRequest";
    public static final String GET_USER_OTHER_SKILL_URL = BaseUri + "/getUserOtherSkill";
    public static final String GET_MY_EVENTS_URL = BaseUri + "/getMyEvents";
    public static final String GET_MY_EVENTS_LIST_URL = BaseUri + "/getMyEventList";
    public static final String GET_ALL_CONTACTS_OF_EVENTS_URL = BaseUri + "/getAllContactsOfEvents";
    public static final String GET_EVENT_DETAILS_URL = BaseUri + "/getEventDetails";
    public static final String GET_EVENT_DETAIL_URL = BaseUri + "/getEventDetail";
    public static final String GET_USER_TAGS_URL = BaseUri + "/getUserTags";
    public static final String GET_NETWORK_URL = BaseUri + "/getNetworkView";
    public static final String GET_MY_MENTEES_URL = BaseUri + "/getMyMenteesList";
    public static final String GET_MY_MENTORS_URL = BaseUri + "/getMyMentorsList";
    public static final String GET_USER_KEY_FROM_TAG_URL = BaseUri + "/getUserKeyFromTag";
    public static final String GET_NETWORK_CONTACTS_URL = BaseUri + "/getNetworkContacts";
    public static final String GET_CONTACTS_URL = BaseUri + "/getContact";
    public static final String GET_CONTACTS_EVENT_URL = BaseUri + "/getContact";
    public static final String GET_CONTACTS_EVENT_LIST_URL = BaseUri + "/getEventAddContactList";
    public static final String GET_USER_CONTACT_URL = BaseUri + "/getUserContact";
    public static final String GET_EVENT_KEY_FROM_EVENT = BaseUri + "/getEventKeyFromEvent";
    public static final String GET_MY_MENTEE = BaseUri + "/getMyMentee";
    public static final String GET_MY_MENTOR = BaseUri + "/getMyMentors";
    public static final String GET_UP_COMMING_EVENTS = BaseUri + "/getMyUpCommingEvents";
    public static final String GET_UP_COMING_EVENT_NOTIFICATION_LIST = BaseUri + "/getUpCommingEventsNotificationList";
    public static final String GET_UP_COMING_EVENT_OTHER_NOTIFICATION_LIST = BaseUri + "/getMenteeMentorRequestList";
    public static final String GET_UP_COMING_EVENT_LIST = BaseUri + "/getMyUpComingEventList";
    public static final String GET_UP_COMMING_EVENTS_KEY = BaseUri + "/getMyUpComingEventsKey";
    public static final String GET_NO_OF_PEOPLE_GOING = BaseUri + "/getNumberOfPeopleGoing";
    public static final String ADD_USER_SKILL_URL = BaseUri + "/addUserSKill";
    public static final String LOCATION_URL = BaseUri + "/setLoc";
    public static final String NAME_URL = BaseUri + "/getName";
    public static final String GET_FACEBOOK_SKILLS= BaseUri + "/getFacebookSkills";
    public static final String GET_USER_CHOICE_EVENT_DETAILS_URL = BaseUri + "/getUserChoiceOnEventDetails";
    public static final String REMOVE_USER_URL = BaseUri + "/removeUser";
    public static final String COMPANY_NAME_URL = BaseUri + "/getCompanyName";
    public static final String REGISTER_URL = BaseUri + "/register";
    public static final String SET_LOCATION_URL = BaseUri + "/setEventLocation";
    public static final String CREATE_EVENT_URL = BaseUri + "/createEvent";
    public static final String CREATE_EVENT_WEIGHT_URL = BaseUri + "/createEventLightWeight";
    public static final String UPDATE_UPCOMMING_EVENTS_SELECTED_URL = BaseUri + "/updateUpcomingEventSelected";
    public static final String UPDATE_PARTICIPANT_STATUS_URL = BaseUri + "/updateParticipantStatus";
    public static final String UPDATE_EVENT_PARTICIPANT_STATUS_URL = BaseUri + "/updateEventParticipantsStatus";
    public static final String UPDATE_EVENT_PARTICIPANTS_URL = BaseUri + "/updateEventParticipants";
    public static final String CHECK_USER_URL = BaseUri + "/checkUser";
    public static final String CHECK_REGISTRATION_URL = BaseUri + "/checkRegisteration";
    public static final String CHECK_SCHOOL_URL = BaseUri + "/checkSchool";
    public static final String ADD_USER_INFO_URL = BaseUri + "/addUserInfo";
    public static final String USER_EMAIL_URL = BaseUri + "/getUserEmail";
    public static final String USER_IMAGE_URL = BaseUri + "/getUserImage";
    public static final String GET_OTHER_PROFILE_DETAILS = BaseUri + "/getOtherProfileDetails";
    public static final String GET_PROFILE_BASIC_DETAILS = BaseUri + "/getProfileBasicDetails";
    public static final String GET_EVENT_END_DATE_URL = BaseUri + "/getEventEndDate";
    public static final String GET_EDUCATION_URL = BaseUri + "/getEducation";
    public static final String GET_TAG_URL = BaseUri + "/getTags";
    public static final String EVENT_DETAILS_URL = BaseUri + "/getEventCreator";

}