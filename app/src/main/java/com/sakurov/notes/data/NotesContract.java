package com.sakurov.notes.data;

import android.net.Uri;

/**
 * Created by sakurov on 31.10.17.
 */

public final class NotesContract {
    public static final class Entry {
        private static final String CONTENT = "content://";

        public static final String AUTHORITY = "com.sakurov.notes";
        public static final String USERS_PATH = "users";
        public static final String NOTES_PATH = "notes";
        public static final String NOTIFICATIONS_PATH = "notifications";

        public static final Uri USERS_URI = Uri.parse(CONTENT + AUTHORITY + "/" + USERS_PATH + "/");
        public static final Uri NOTES_URI = Uri.parse(CONTENT + AUTHORITY + "/" + NOTES_PATH + "/");
        public static final Uri NOTIFICATIONS_URI = Uri.parse(CONTENT + AUTHORITY + "/" + NOTIFICATIONS_PATH + "/");

        public static final String NOTE_ID_PATH = CONTENT + AUTHORITY + "/" + NOTES_PATH + "/";
        public static final String NOTIFICATION_ID_PATH = CONTENT + AUTHORITY + "/" + NOTIFICATIONS_PATH + "/";

    }
}
