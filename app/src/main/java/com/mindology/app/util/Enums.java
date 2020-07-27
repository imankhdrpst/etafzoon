package com.mindology.app.util;

import android.text.TextUtils;
import android.util.Log;

import java.util.EnumMap;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class Enums {

    private static EnumMap<PropertyTypes, String> propertyTypeEnumMap;
    private static EnumMap<Furnishing, String> furnishingStringEnumMap;
    private static HashMap<HotSpotPriority, String> priorityStrings = null;

    public static EnumMap<Enums.Furnishing, String> getFurnishingEnumMap() {
        if (furnishingStringEnumMap == null) {
            furnishingStringEnumMap = new EnumMap<Enums.Furnishing, String>(Enums.Furnishing.class);
            furnishingStringEnumMap.put(Furnishing.UNFURNISHED, "None");
            furnishingStringEnumMap.put(Furnishing.PART_FURNISHED, "Part");
            furnishingStringEnumMap.put(Furnishing.FULLY_FURNISHED, "Full");
        }

        return furnishingStringEnumMap;
    }

    public enum InspectionStatus {
        ASSIGNED,
        STARTED,
        CANCELLED,
        CLOSED,
        COMPLETED,
        SIGNED;

        public static InspectionStatus getRandomStatus() {
            Random random = new Random();
            return values()[random.nextInt(values().length)];
        }

        public String getJson() {
            String json = "{\"state\":\"" + name() + "\"}";
            Log.v("OK2", "json is:" + json);
            return json;
        }
    }

    public enum InspectionTypes {
        NONE,
        CHECK_IN,
        CHECK_OUT,
        MID_TERM;

        public static InspectionTypes getRandomType() {
            Random random = new Random();
            return values()[random.nextInt(values().length)];
        }

        public static String getWellFormedString(InspectionTypes type) {
            if (type == null) return null;
            String str = type.name().replace("_", "-");
            if (str == null) {
                return null;
            }

            boolean space = true;
            StringBuilder builder = new StringBuilder(str);
            final int len = builder.length();

            for (int i = 0; i < len; ++i) {
                char c = builder.charAt(i);
                if (space) {
                    if (!Character.isWhitespace(c)) {
                        // Convert to title case and switch out of whitespace mode.
                        builder.setCharAt(i, Character.toTitleCase(c));
                        space = false;
                    }
                } else if (Character.isWhitespace(c)) {
                    space = true;
                } else {
                    builder.setCharAt(i, Character.toLowerCase(c));
                }
            }

            return builder.toString();
        }

        public static InspectionTypes getRelevantType(String name) {
            if (TextUtils.isEmpty(name))
                return null;
            name = name.toUpperCase().replace("-", "_");
            return InspectionTypes.valueOf(name);
        }
    }

    public enum PropertyTypes {
        APARTMENT,
        BAR,
        BARN,
        BEDSIT,
        BUNGALOW,
        COMMERCIAL,
        CONDOMINIUM,
        COTTAGE,
        DUPLEX,
        FLAT_CONVERTED,
        FLAT_PURPOSE_BUILD,
        GOVERNMENT,
        HMO,
        HOUSE,
        INDUSTRIAL,
        MAISONETTE,
        MANSION,
        MULTI_UNIT_BUILDING,
        OFFICE,
        OTHER,
        PUBLIC_SPACE,
        RESTAURANT,
        RETAIL,
        ROOM,
        STORAGE,
        STUDIO_APARTMENT,
        TENEMENT,
        TOWNHOUSE,
        WAREHOUSE;

        public static PropertyTypes getTypeByValue(String name) {
            if (TextUtils.isEmpty(name)) return null;
            if (propertyTypeEnumMap == null) {
                getPropertyTypesEnumMap();
            }
            for (PropertyTypes type : propertyTypeEnumMap.keySet()) {
                if (name.toLowerCase().equals(propertyTypeEnumMap.get(type).toLowerCase())) {
                    return type;
                }
            }
            return null;
        }

        public static PropertyTypes getTypeByKey(String name) {
            if (TextUtils.isEmpty(name)) return null;
            for (PropertyTypes type : propertyTypeEnumMap.keySet()) {
                if (name.toLowerCase().equals(type.name().toLowerCase())) {
                    return type;
                }
            }
            return null;
        }

        public static String getNameByType(PropertyTypes type) {
            if (type == null) return null;
            return propertyTypeEnumMap.get(type);
        }

    }

    public enum Furnishing {
        UNFURNISHED,
        PART_FURNISHED,
        FULLY_FURNISHED;

        public static Furnishing getTypeByValue(String name) {
            if (TextUtils.isEmpty(name)) return null;
            for (Furnishing typeKey : furnishingStringEnumMap.keySet()) {
                if (name.toLowerCase().equals(furnishingStringEnumMap.get(typeKey).toLowerCase())) {
                    return typeKey;
                }
            }
            return null;
        }

        public static Furnishing getTypeByKey(String name) {
            if (TextUtils.isEmpty(name)) return null;
            if (furnishingStringEnumMap == null) {
                getFurnishingEnumMap();
            }
            for (Furnishing type : furnishingStringEnumMap.keySet()) {
                if (name.toLowerCase().equals(type.name().toLowerCase())) {
                    return type;
                }
            }
            return null;
        }

        public static String getNameByType(Furnishing type) {
            if (type == null) return null;
            return furnishingStringEnumMap.get(type);
        }
    }

    public static EnumMap<Enums.PropertyTypes, String> getPropertyTypesEnumMap() {
        if (propertyTypeEnumMap == null) {
            propertyTypeEnumMap = new EnumMap<Enums.PropertyTypes, String>(Enums.PropertyTypes.class);
//            propertyTypeEnumMap.put(Enums.PropertyTypes.NONE, "None");
            propertyTypeEnumMap.put(Enums.PropertyTypes.APARTMENT, "Apartment");
            propertyTypeEnumMap.put(Enums.PropertyTypes.BAR, "Bar");
            propertyTypeEnumMap.put(Enums.PropertyTypes.BARN, "Barn");
            propertyTypeEnumMap.put(Enums.PropertyTypes.BEDSIT, "Bedsit");
            propertyTypeEnumMap.put(Enums.PropertyTypes.BUNGALOW, "Bungalow");
            propertyTypeEnumMap.put(Enums.PropertyTypes.COMMERCIAL, "Commercial");
            propertyTypeEnumMap.put(Enums.PropertyTypes.CONDOMINIUM, "Condominium");
            propertyTypeEnumMap.put(Enums.PropertyTypes.COTTAGE, "Cottage");
            propertyTypeEnumMap.put(Enums.PropertyTypes.DUPLEX, "Duplex");
            propertyTypeEnumMap.put(Enums.PropertyTypes.FLAT_CONVERTED, "Flat Converted");
            propertyTypeEnumMap.put(Enums.PropertyTypes.FLAT_PURPOSE_BUILD, "Flat Purpose-built");
            propertyTypeEnumMap.put(Enums.PropertyTypes.GOVERNMENT, "Government");
            propertyTypeEnumMap.put(Enums.PropertyTypes.HMO, "HMO");
            propertyTypeEnumMap.put(Enums.PropertyTypes.HOUSE, "House");
            propertyTypeEnumMap.put(Enums.PropertyTypes.INDUSTRIAL, "Industrial");
            propertyTypeEnumMap.put(Enums.PropertyTypes.MAISONETTE, "Maisonette");
            propertyTypeEnumMap.put(Enums.PropertyTypes.MANSION, "Mansion");
            propertyTypeEnumMap.put(Enums.PropertyTypes.MULTI_UNIT_BUILDING, "Multi-unit Building");
            propertyTypeEnumMap.put(Enums.PropertyTypes.OFFICE, "Office");
            propertyTypeEnumMap.put(Enums.PropertyTypes.OTHER, "Other");
            propertyTypeEnumMap.put(Enums.PropertyTypes.PUBLIC_SPACE, "Public Space");
            propertyTypeEnumMap.put(Enums.PropertyTypes.RESTAURANT, "Restaurant");
            propertyTypeEnumMap.put(Enums.PropertyTypes.RETAIL, "Retail");
            propertyTypeEnumMap.put(Enums.PropertyTypes.ROOM, "Room");
            propertyTypeEnumMap.put(Enums.PropertyTypes.STORAGE, "Storage");
            propertyTypeEnumMap.put(Enums.PropertyTypes.STUDIO_APARTMENT, "Studio Apartment");
            propertyTypeEnumMap.put(Enums.PropertyTypes.TENEMENT, "Tenement");
            propertyTypeEnumMap.put(Enums.PropertyTypes.TOWNHOUSE, "Townhouse");
            propertyTypeEnumMap.put(Enums.PropertyTypes.WAREHOUSE, "Warehouse");
        }

        return propertyTypeEnumMap;
    }

    public static HashMap<HotSpotPriority, String> getHotSpotPriorityMap()
    {
        priorityStrings = new HashMap<>();
        priorityStrings.put(HotSpotPriority.MAINTENANCE, "Repair / Maintenance");
        priorityStrings.put(HotSpotPriority.BEYOND_FAIR, "Beyond fair wear & tear");
        priorityStrings.put(HotSpotPriority.MISSING, "Missing");
        priorityStrings.put(HotSpotPriority.REPLACING, "Replacing");
        priorityStrings.put(HotSpotPriority.REQUIRES_CLEANING, "Requires cleaning");

        return priorityStrings;
    }

    public enum FilterOperators {
        eq, // equlas to
        ne, // not equals to
        gt, // greater than
        ge, // grater than or equal
        lt, // less than
        le, // less than or equal
        isNull, // is null
        isNotNull, // is not null
        like, // like
        startsWith, // starts with
        endsWith, // ends with
        in, // is in set
        notIn, // is not in set
        stringContainsIgnoreCase,

    }

    public enum SortOrder {
        ASC,
        DESC
    }

    public enum AssetType {
        AREA,
        METER,
        ALARM
    }

    public enum AlarmStatus {
        NOT_TESTED,
        NOT_WORKING,
        NOTED
    }

    public enum HotSpotPriority {
        MAINTENANCE,
        BEYOND_FAIR,
        REPLACING,
        MISSING,
        REQUIRES_CLEANING;

        public static int getIndexOf(HotSpotPriority name) {
            for (int i = 0; i < HotSpotPriority.values().length; i++) {
                if (name == HotSpotPriority.values()[i]) {
                    return i;
                }
            }
            return 0;
        }

        public static String getCaptionByName(HotSpotPriority p)
        {
            return getHotSpotPriorityMap().get(p);
        }

        public static HotSpotPriority getNameByCaption(String caption)
        {
            for (Map.Entry<HotSpotPriority, String> item : getHotSpotPriorityMap().entrySet())
            {
                if (item.getValue().equals(caption))
                {
                    return item.getKey();
                }
            }
            return null;
        }


    }

    public enum EducationType
    {
        BACHELOR, MA, RESEARCH
        }

    public enum MarriageStatus
    {
        SINGLE, MARRIED
    }

}
