package com.mindology.app.repo;

import com.mindology.app.models.ClientUserDTO;
import com.mindology.app.models.Post;

public class TempDataHolder {
    private static ClientUserDTO currentUser;
    private static Post selectedPost;


    public static void setCurrentUser(ClientUserDTO currentUser) {
        TempDataHolder.currentUser = currentUser;
    }

    public static ClientUserDTO getCurrentUser() {
        return currentUser;
    }


    public static void resetAllData() {
        currentUser = null;
    }

    public static Post getSelectedPost() {
        return selectedPost;
    }

    public static void setSelectedPost(Post selectedPost) {
        TempDataHolder.selectedPost = selectedPost;
    }
}
