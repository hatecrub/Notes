package com.sakurov.notes.data;

import android.net.Uri;

final class NotesContract {

    static final class Entry {

        private static final String CONTENT = "content://";

        static final String AUTHORITY = "com.sakurov.notes";
        static final String USERS_PATH = "users";
        static final String NOTES_PATH = "notes";
        static final String NOTIFICATIONS_PATH = "notifications";

        static final Uri USERS_URI = Uri.parse(CONTENT + AUTHORITY + "/" + USERS_PATH + "/");
        static final Uri NOTES_URI = Uri.parse(CONTENT + AUTHORITY + "/" + NOTES_PATH + "/");
        static final Uri NOTIFICATIONS_URI = Uri.parse(CONTENT + AUTHORITY + "/" + NOTIFICATIONS_PATH + "/");

        static final String NOTE_ID_PATH = CONTENT + AUTHORITY + "/" + NOTES_PATH + "/";
        static final String NOTIFICATION_ID_PATH = CONTENT + AUTHORITY + "/" + NOTIFICATIONS_PATH + "/";
    }
}
