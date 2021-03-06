package com.epfd.dolto.api;

import com.epfd.dolto.models.Event;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class EventHelper {

    private static final String COLLECTION_NAME = "events";
    public static final String ROOT_UID = "EVENT";

    // --- COLLECTION REFERENCE ---

    public static CollectionReference getEventsCollection(){
        return FirebaseFirestore.getInstance().collection(COLLECTION_NAME);
    }

    // --- CREATE ---

    public static Task<Void> createEvent(String uid, Event event) {
        return EventHelper.getEventsCollection().document(uid).set(event);
    }

    // --- GET ---

    public static Task<DocumentSnapshot> getEvent(String uid){
        return EventHelper.getEventsCollection().document(uid).get();
    }

    // --- UPDATE ---

    public static Task<Void> updateEventName(String uid, String name){
        return EventHelper.getEventsCollection().document(uid).update("name", name);
    }

    public static Task<Void> updateEventDate(String uid, String date){
        return EventHelper.getEventsCollection().document(uid).update("date", date);
    }

    public static Task<Void> updateEventDescription(String uid, String description){
        return EventHelper.getEventsCollection().document(uid).update("description", description);
    }

    public static Task<Void> updateEventPhoto(String uid, String photo){
        return EventHelper.getEventsCollection().document(uid).update("photo", photo);
    }

    public static Task<Void> updateEventLogo(String uid, String label){
        return EventHelper.getEventsCollection().document(uid).update("logo", label);
    }

    public static Task<Void> updateEventStages(String uid, String stages){
        return EventHelper.getEventsCollection().document(uid).update("stages", stages);
    }

    public static Task<Void> updateEventNeeds(String uid, String needs){
        return EventHelper.getEventsCollection().document(uid).update("needs", needs);
    }

    public static Task<Void> updateEventVisibility(String uid, boolean state){
        return EventHelper.getEventsCollection().document(uid).update("affichage", state);
    }

    // --- DELETE ---

    public static Task<Void> deleteEvent(String uid) {
        return EventHelper.getEventsCollection().document(uid).delete();
    }

}
