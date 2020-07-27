package com.mindology.app.repo;

import com.mindology.app.models.Alarm;
import com.mindology.app.models.Area;
import com.mindology.app.models.Inspection;
import com.mindology.app.models.Meter;
import com.mindology.app.models.Panorama;
import com.mindology.app.models.User;

public class TempDataHolder {
    private static Inspection currentInspection;
    private static User currentUser;
    private static Area selectedArea;
    private static Meter selectedMeter;
    private static Alarm selectedAlarm;
    private static Panorama selectedPanorama;



    public static void setCurrentInspection(Inspection selectedInspection) {

        currentInspection=(selectedInspection);
    }


    public static Inspection getCurrentInspection()
    {
        return currentInspection;
    }



    public static void setCurrentUser(User currentUser) {
        TempDataHolder.currentUser = currentUser;
    }

    public static User getCurrentUser() {
        return currentUser;
    }

    public static void setSelectedArea(Area selectedArea) {
        TempDataHolder.selectedArea = selectedArea;
    }

    public static Area getSelectedArea() {
        return selectedArea;
    }

    public static Meter getSelectedMeter() {
        return selectedMeter;
    }

    public static void setSelectedMeter(Meter meter) {
        TempDataHolder.selectedMeter = meter;
    }

    public static Alarm getSelectedAlarm() {
        return selectedAlarm;
    }

    public static void setSelectedAlarm(Alarm alarm) {
        TempDataHolder.selectedAlarm = alarm;
    }

    public static void setSelectedPanorama(Panorama selectedPanorama) {
        TempDataHolder.selectedPanorama = selectedPanorama;
    }

    public static Panorama getSelectedPanorama() {
        return selectedPanorama;
    }

    public static void resetAllData() {
        currentUser = null;
        selectedArea = null;
        selectedMeter = null;
        selectedAlarm = null;
        selectedPanorama = null;
        currentInspection = null;
    }

}
