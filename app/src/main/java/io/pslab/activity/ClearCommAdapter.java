package io.pslab.activity;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.pslab.R;

public class ClearCommAdapter extends RecyclerView.Adapter<ClearCommAdapter.ClearCommHolder> {

    /**
     README - INSTRUCTIONS

     The Clear Communications Privacy Tool uses a DataAccessDescriptor object to store and manage
     the information you enter regarding how this app accesses the user's data.

     To use ClearCommPrivacy, you need to simply create a DataAccessDescriptor instance for each
     different type of data the app will access.

     To handle this, there is a template ArrayList below, which contains multiple examples of
     various types of data accesses.

     Please create each of your DataAccessDescriptor instances in the ArrayList below, using the
     instructions and examples, and then delete or comment out the examples and instructions.

     Please note that your DataAccessDescriptors must be contained by the dataAccessDescriptors
     ArrayList to be included in the user-facing UI. Also, if you create a DataAccessDescriptor
     with a permission not requested by the app, or if the app requests a permission that you do
     not create a DataAccessDescriptor for, there will be a warning listed in the user facing UI.

     */


    ArrayList<DataAccessDescriptor> dataAccessDescriptors = new ArrayList<DataAccessDescriptor>() {{
        add(new DataAccessDescriptor(
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                "Edit External Storage",
                "save data logs to the phone storage",
                false,
                false,
                "N/A",
                false,
                "N/A"
        ));
        add(new DataAccessDescriptor(
                Manifest.permission.READ_EXTERNAL_STORAGE,
                "Read External Storage",
                "read data logs from the phone storage",
                false,
                false,
                "N/A",
                false,
                "N/A"
        ));
        add(new DataAccessDescriptor(
                Manifest.permission.INTERNET,
                "Internet Access",
                "read data logs from the phone storage",
                false,
                false,
                "N/A",
                false,
                "N/A"
        ));
        add(new DataAccessDescriptor(
                Manifest.permission.RECORD_AUDIO,
                "Device Surroundings",
                "read data logs from the phone storage",
                false,
                false,
                "N/A",
                false,
                "N/A"
        ));
        add(new DataAccessDescriptor(
                Manifest.permission.ACCESS_FINE_LOCATION,
                "Device Fine Location",
                "provide the user with a map and compass for various activities",
                false,
                false,
                "N/A",
                false,
                "N/A"
        ));
        add(new DataAccessDescriptor(
                Manifest.permission.BLUETOOTH,
                "Connection to Bluetooth Devices",
                "connect to various PSLab sensors and devices",
                false,
                false,
                "N/A",
                false,
                "N/A"
        ));
        add(new DataAccessDescriptor(
                Manifest.permission.BLUETOOTH_ADMIN,
                "Bluetooth Administration",
                "manage bluetooth connections such as turning connections on and off",
                false,
                false,
                "N/A",
                false,
                "N/A"
        ));
        add(new DataAccessDescriptor(
                Manifest.permission.ACCESS_COARSE_LOCATION,
                "Device Course Location",
                "provide the user with a map and compass for various activities",
                false,
                false,
                "N/A",
                false,
                "N/A"
        ));
        add(new DataAccessDescriptor(
                "android.permission.ACCESS_BACKGROUND_LOCATION",
                "Device Location",
                "provide the user with a map and compass for various activities",
                true,
                false,
                "N/A",
                false,
                "N/A"
        ));
        add(new DataAccessDescriptor(
                "android.permission.ACCESS_MEDIA_LOCATION",
                "Device Storage Locations",
                "determine whether there is device or memory card storage in order to store data logs",
                false,
                false,
                "N/A",
                false,
                "N/A"
        ));
    }};


    /* DO NOT EDIT ANY CODE BELOW THIS LINE */



    private RecyclerView.RecycledViewPool viewPool = new RecyclerView.RecycledViewPool();
    private Context context;
    private List<PermissionGroup> permissionGroups;

    public ClearCommAdapter(Context context) {
        setContext(context);
        setPermissionGroups(new ArrayList<PermissionGroup>());
        setInitialListToDisplay();
    }

    public Context getContext() {
        return context;
    }

    public void setContext(Context context) {
        this.context = context;
    }

    public ArrayList<DataAccessDescriptor> getDataAccessDescriptors() {
        return dataAccessDescriptors;
    }

    public void setDataAccessDescriptors(List<DataAccessDescriptor> dataAccessDescriptors) {
        this.dataAccessDescriptors = (ArrayList<DataAccessDescriptor>) dataAccessDescriptors;
        Collections.sort(this.dataAccessDescriptors);
    }

    public void addDataAccessDescriptors(List<DataAccessDescriptor> newDataAccessDescriptors) {
        this.dataAccessDescriptors.addAll(newDataAccessDescriptors);
        Collections.sort(this.dataAccessDescriptors);
    }

    public List<PermissionGroup> getPermissionGroups() {
        return permissionGroups;
    }

    public void setPermissionGroups(List<PermissionGroup> permissionGroups) {
        this.permissionGroups = permissionGroups;
    }

    public void addPermissionGroup(PermissionGroup permissionGroup) {
        this.permissionGroups.add(permissionGroup);
        Collections.sort(this.permissionGroups);
    }

    /**
     * Determines if there are any permissions either requested but
     * not declared in ClearComm or declared in ClearComm but not Requested
     */
    private void setInitialListToDisplay() {
        //For loop iterates over each DataAccessDescriptor in the main list
        for (DataAccessDescriptor dataAccessDescriptor : getDataAccessDescriptors())
        {
            //Checks to see if the relevant permission for each DataAccessDescriptor is requested by the app
            if(!(isPermissionRequestedByApp(dataAccessDescriptor.getRelevantPermission())))
            {
                //If the given DataAccessDescriptor is not requested by the app,
                //change the reasoning for the data access to show this fact
                dataAccessDescriptor.setErrorString("The developer specified this data is collected using the " + dataAccessDescriptor.getRelevantPermission() + " permission, but the app never actually requests this permission.");
            }
        }
        //Gets a list of DataAccessDescriptors for permissions the app requests but are not listed in the main list
        //We then add these to the main list
        addDataAccessDescriptors(getListOfPermissionsRequestedByAppButNotDescribedByDeveloper());

        for (DataAccessDescriptor dataAccessDescriptor : getDataAccessDescriptors()) {
            boolean permissionGroupExists = false;
            for (PermissionGroup permissionGroup : getPermissionGroups()) {
                if (dataAccessDescriptor.getRelevantPermissionGroup().equals(permissionGroup.getPermissionGroupTitle())) {
                    permissionGroup.addDataAccessDescriptor(dataAccessDescriptor);
                    permissionGroupExists = true;
                }
            }
            if (!permissionGroupExists) {
                PermissionGroup newPermissionGroup = new PermissionGroup(dataAccessDescriptor.getRelevantPermissionGroup(), dataAccessDescriptor);
                addPermissionGroup(newPermissionGroup);
            }
        }
    }

    /**
     * Determines whether there are any permissions requested by the app but not described in the main list,
     * and then returns a list of DataDescriptors containing those permissions.
     *
     * @return List of DataAccessDescriptors for each permission requested by the app but that is not in the main list
     */
    public List<DataAccessDescriptor> getListOfPermissionsRequestedByAppButNotDescribedByDeveloper()
    {
        ArrayList<DataAccessDescriptor> declaredButNotDescribed = new ArrayList<>();
        PackageInfo info;
        try {
            info = getContext().getPackageManager().getPackageInfo(getContext().getPackageName(), PackageManager.GET_PERMISSIONS);
            String[] requestedPermissions = info.requestedPermissions; //This array contains the requested permissions.
            if(requestedPermissions != null && requestedPermissions.length > 0) {
                for (String requestedPermission : requestedPermissions) {
                    if (!hasDataAccessDescriptorForPermission(requestedPermission)) {
                        DataAccessDescriptor newDataAccessDescriptor = new DataAccessDescriptor(
                                requestedPermission, "No Data Specified",
                                "Unknown", false,
                                false, null,
                                false, null);
                        newDataAccessDescriptor.setErrorString("The app requests the " + requestedPermission
                                + " permission, but the developer did not specify what data they collect using this permission");
                        declaredButNotDescribed.add(newDataAccessDescriptor);
                    }
                }
            }
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return declaredButNotDescribed;
    }

    /**
     * Checks to see whether a given permission is contained in the main list
     *
     * @param permission - the permission to check
     * @return true if the permission is in the main list, false otherwise
     */
    public boolean hasDataAccessDescriptorForPermission(String permission)
    {
        for(DataAccessDescriptor dataAccessDescriptor : getDataAccessDescriptors()) {
            if (dataAccessDescriptor.getRelevantPermission().equals(permission)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Checks to see whether a given permission is requested by the app by being listed in the app's manifest
     *
     * @param permission - the permission to check
     * @return true if the permission is requested by the app, false otherwise
     */
    public boolean isPermissionRequestedByApp(String permission)
    {
        try {
            PackageInfo info = getContext().getPackageManager().getPackageInfo(getContext().getPackageName(), PackageManager.GET_PERMISSIONS);
            if (info.requestedPermissions != null) {
                for (String p : info.requestedPermissions) {
                    if (p.equals(permission)) {
                        return true;
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    @NonNull
    @Override
    public ClearCommHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.clear_comm_card_view, viewGroup, false);
        return new ClearCommHolder(view);
    }

    /**
     * Converts the data from each DataAccessDescriptor into plain english and formats the data in the end-user UI
     *
     * This method handles most of the processing between the developer inputs and the final end-user UI
     *
     * @param myHolder The holder for all of the relevant views in the recycler view
     * @param position The index of the current DataAccessDescriptor being uploaded to the End-User UI
     */
    @Override
    public void onBindViewHolder(@NonNull ClearCommHolder myHolder, int position) {
        PermissionGroup permissionGroup = getPermissionGroups().get(position);

        // Sets the Heading of the Permission Group Card
        myHolder.relevantPermission.setText(permissionGroup.getPermissionGroupTitle());

        // Calls the Internal Adapter to handle the Permission Group Card Contents
        LinearLayoutManager layoutManager = new LinearLayoutManager(myHolder.dataDetailEntries.getContext());
        layoutManager.setInitialPrefetchItemCount(permissionGroup.getDataAccessDescriptors().size());
        ClearCommInternalAdapter childItemAdapter = new ClearCommInternalAdapter(permissionGroup.getDataAccessDescriptors());
        myHolder.dataDetailEntries.setLayoutManager(layoutManager);
        myHolder.dataDetailEntries.setAdapter(childItemAdapter);
        myHolder.dataDetailEntries.setRecycledViewPool(viewPool);
    }

    @Override
    public int getItemCount() {
        return getPermissionGroups().size();
    }


    /**
     * The ClearCommHolder is used to create the card for each DataAccessDescriptor
     */
    public static class ClearCommHolder extends RecyclerView.ViewHolder{

        TextView relevantPermission;
        private RecyclerView dataDetailEntries;

        public ClearCommHolder(@NonNull View itemView) {
            super(itemView);
            this.relevantPermission = itemView.findViewById(R.id.relevantPermission);
            this.dataDetailEntries = itemView.findViewById(R.id.dataDetailEntries);
        }
    }
}

class PermissionGroup implements Comparable<PermissionGroup>{
    // Declaration of the variables
    private String permissionGroupTitle;
    private List<DataAccessDescriptor> dataAccessDescriptors;


    public PermissionGroup(String permissionGroupTitle, DataAccessDescriptor dataAccessDescriptor) {
        this.permissionGroupTitle = permissionGroupTitle;
        ArrayList<DataAccessDescriptor> dataAccessDescriptors = new ArrayList<>();
        dataAccessDescriptors.add(dataAccessDescriptor);
        this.dataAccessDescriptors = dataAccessDescriptors;
    }
    // Constructor of the class
    // to initialize the variables
    public PermissionGroup(String permissionGroupTitle, List<DataAccessDescriptor> dataAccessDescriptors) {
        this.permissionGroupTitle = permissionGroupTitle;
        this.dataAccessDescriptors = dataAccessDescriptors;
    }
    public String getPermissionGroupTitle() {
        return permissionGroupTitle;
    }

    public void setPermissionGroupTitle(String permissionGroupTitle) {
        this.permissionGroupTitle = permissionGroupTitle;
    }

    public List<DataAccessDescriptor> getDataAccessDescriptors() {
        return dataAccessDescriptors;
    }

    public void setDataAccessDescriptors(List<DataAccessDescriptor> dataAccessDescriptors) {
        this.dataAccessDescriptors = dataAccessDescriptors;
    }

    public void addDataAccessDescriptor(DataAccessDescriptor dataAccessDescriptor) {
        this.dataAccessDescriptors.add(dataAccessDescriptor);
    }

    @Override
    public int compareTo(PermissionGroup o) {
        return this.getPermissionGroupTitle().compareTo(o.getPermissionGroupTitle());
    }
}

class DataAccessDescriptor implements Comparable<DataAccessDescriptor> {
    private String relevantPermission;
    private String relevantPermissionGroup;
    private String descriptionOfDataAccessed;
    private String reasoningBehindDataAccession;
    private boolean dataAccessedDuringBackgroundUsage;
    private boolean sharedWithAppCompany;
    private String reasonSharedWithAppCompany; //reason only required if sharedWithAppCompany is true
    private boolean sharedWithThirdPartyCompany;
    private String reasonSharedWithThirdPartyCompany; //reason only required if sharedWithThirdPartyCompany is true
    private String errorString;

    private final static Map<String, String> dangerousPermissionGroups = new HashMap<String, String>() {
        {
            put(Manifest.permission.READ_CALENDAR, "Calendar");
            put(Manifest.permission.WRITE_CALENDAR, "Calendar");
            put(Manifest.permission.CAMERA, "Camera");
            put(Manifest.permission.READ_CONTACTS, "Contacts");
            put(Manifest.permission.WRITE_CONTACTS, "Contacts");
            put(Manifest.permission.GET_ACCOUNTS, "Contacts");
            put(Manifest.permission.ACCESS_FINE_LOCATION, "Location");
            put(Manifest.permission.ACCESS_COARSE_LOCATION, "Location");
            put(Manifest.permission.RECORD_AUDIO, "Microphone");
            put(Manifest.permission.READ_PHONE_STATE, "Phone");
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                put(Manifest.permission.READ_PHONE_NUMBERS, "Phone");
            }
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                put(Manifest.permission.ANSWER_PHONE_CALLS, "Phone");
            }
            put(Manifest.permission.READ_CALL_LOG, "Call Log");
            put(Manifest.permission.WRITE_CALL_LOG, "Call Log");
            put(Manifest.permission.ADD_VOICEMAIL, "Phone");
            put(Manifest.permission.USE_SIP, "Phone");
            put(Manifest.permission.PROCESS_OUTGOING_CALLS, "Phone");
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT_WATCH) {
                put(Manifest.permission.BODY_SENSORS, "Sensors");
            }
            put(Manifest.permission.SEND_SMS, "SMS");
            put(Manifest.permission.READ_SMS, "SMS");
            put(Manifest.permission.RECEIVE_SMS, "SMS");
            put(Manifest.permission.RECEIVE_WAP_PUSH, "SMS");
            put(Manifest.permission.RECEIVE_MMS, "SMS");
            put(Manifest.permission.READ_EXTERNAL_STORAGE, "Storage");
            put(Manifest.permission.WRITE_EXTERNAL_STORAGE, "Storage");
            /*if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                put(Manifest.permission.ACCESS_MEDIA_LOCATION, "Storage");
            }
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
                put(Manifest.permission.ACCEPT_HANDOVER, "UNKNOWN PERMISSION GROUP");
            }
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                put(Manifest.permission.ACCESS_BACKGROUND_LOCATION, "UNKNOWN PERMISSION GROUP");
            }
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                put(Manifest.permission.ACTIVITY_RECOGNITION, "Activity Recognition");
            }*/
        }
    };


    public DataAccessDescriptor(@NonNull String relevantPermission, @NonNull String descriptionOfDataAccessed, @NonNull String reasoningBehindDataAccession, boolean dataAccessedDuringBackgroundUsage, boolean sharedWithAppCompany, String reasonSharedWithAppCompany, boolean sharedWithThirdPartyCompany, String reasonSharedWithThirdPartyCompany) {
        setRelevantPermission(relevantPermission);
        setDescriptionOfDataAccessed(descriptionOfDataAccessed);
        setReasoningBehindDataAccession(reasoningBehindDataAccession);
        setDataAccessedDuringBackgroundUsage(dataAccessedDuringBackgroundUsage);
        setSharedWithAppCompany(sharedWithAppCompany);
        setReasonSharedWithAppCompany(reasonSharedWithAppCompany);
        setSharedWithThirdPartyCompany(sharedWithThirdPartyCompany);
        setReasonSharedWithThirdPartyCompany(reasonSharedWithThirdPartyCompany);
        setRelevantPermissionGroup(DataAccessDescriptor.dangerousPermissionGroups.get(relevantPermission) != null ? DataAccessDescriptor.dangerousPermissionGroups.get(relevantPermission) : "Non-Dangerous Permission");
    }

    public String getRelevantPermission() {
        return relevantPermission;
    }

    public void setRelevantPermission(String permission) {
        this.relevantPermission = permission;
    }

    public String getDescriptionOfDataAccessed() {
        return descriptionOfDataAccessed;
    }

    public void setDescriptionOfDataAccessed(String descriptionOfDataAccessed) {
        this.descriptionOfDataAccessed = descriptionOfDataAccessed;
    }

    public String getReasoningBehindDataAccession() {
        return reasoningBehindDataAccession;
    }

    public void setReasoningBehindDataAccession(String reasoningBehindDataAccession) {
        this.reasoningBehindDataAccession = reasoningBehindDataAccession;
    }

    public boolean isDataAccessedDuringBackgroundUsage() {
        return dataAccessedDuringBackgroundUsage;
    }

    public void setDataAccessedDuringBackgroundUsage(boolean dataAccessedDuringBackgroundUsage) {
        this.dataAccessedDuringBackgroundUsage = dataAccessedDuringBackgroundUsage;
    }

    public boolean isSharedWithAppCompany() {
        return sharedWithAppCompany;
    }

    public void setSharedWithAppCompany(boolean sharedWithAppCompany) {
        this.sharedWithAppCompany = sharedWithAppCompany;
    }

    public String getReasonSharedWithAppCompany() {
        return reasonSharedWithAppCompany;
    }

    public void setReasonSharedWithAppCompany(String reasonSharedWithAppCompany) {
        this.reasonSharedWithAppCompany = reasonSharedWithAppCompany;
    }

    public boolean isSharedWithThirdPartyCompany() {
        return sharedWithThirdPartyCompany;
    }

    public void setSharedWithThirdPartyCompany(boolean sharedWithThirdPartyCompany) {
        this.sharedWithThirdPartyCompany = sharedWithThirdPartyCompany;
    }

    public String getReasonSharedWithThirdPartyCompany() {
        return reasonSharedWithThirdPartyCompany;
    }

    public void setReasonSharedWithThirdPartyCompany(String reasonSharedWithThirdPartyCompany) {
        this.reasonSharedWithThirdPartyCompany = reasonSharedWithThirdPartyCompany;
    }

    @Override
    public String toString() {
        String returnString = "This app uses the " + getRelevantPermission() + " permission to access your " + getDescriptionOfDataAccessed() + " in order to " + getReasoningBehindDataAccession() + ". This data is shared with ";
        if(isSharedWithAppCompany() && isSharedWithThirdPartyCompany()) {
            returnString += "the app company's server in order to " + getReasonSharedWithAppCompany() + " and is shared with 3rd party companies in order to " + getReasonSharedWithThirdPartyCompany() + ",";
        }
        else if(isSharedWithAppCompany()) {
            returnString += "the app company's server in order to " + getReasonSharedWithAppCompany() + ",";
        }
        else if(isSharedWithThirdPartyCompany()) {
            returnString += "3rd party companies in order to " + getReasonSharedWithThirdPartyCompany() + ",";
        }
        else {
            returnString += "no one,";
        }
        if(isDataAccessedDuringBackgroundUsage()) {
            returnString += " and this data may be accessed while you are not actively using the app.";
        }
        else {
            returnString += " and this data will only be accessed while you are actively using the app.";
        }
        return returnString;
    }

    @Override
    public int compareTo(DataAccessDescriptor o) {
        return this.getRelevantPermission().compareTo(o.getRelevantPermission());
    }

    public String getRelevantPermissionGroup() {
        return relevantPermissionGroup;
    }

    public void setRelevantPermissionGroup(String relevantPermissionGroup) {
        this.relevantPermissionGroup = relevantPermissionGroup;
    }

    public String getErrorString() {
        return errorString;
    }

    public void setErrorString(String errorString) {
        this.errorString = errorString;
    }
}

class ClearCommInternalAdapter extends RecyclerView.Adapter<ClearCommInternalAdapter.ClearCommHolder> {

    private List<DataAccessDescriptor> dataAccessDescriptors;
    private static final int TYPE_REGULAR = 1;
    private static final int TYPE_MISSING_DATA = 2;

    public ClearCommInternalAdapter(List<DataAccessDescriptor> dataAccessDescriptors) {
        this.dataAccessDescriptors = dataAccessDescriptors;
    }

    public List<DataAccessDescriptor> getDataAccessDescriptors() {
        return dataAccessDescriptors;
    }

    public void setDataAccessDescriptors(List<DataAccessDescriptor> dataAccessDescriptors) {
        this.dataAccessDescriptors = dataAccessDescriptors;
    }

    @NonNull
    @Override
    public ClearCommInternalAdapter.ClearCommHolder onCreateViewHolder(@NonNull ViewGroup viewGroup,
                                                                       int viewType) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.clear_comm_data_detail_list,
                        viewGroup, false);

        return new ClearCommHolder(view);
    }

    @Override
    public int getItemViewType(int position) {
        if (dataAccessDescriptors.get(position).getErrorString() == null) {
            System.out.println("Regular");
            return TYPE_REGULAR;
        } else {
            System.out.println("Missing Data");
            return TYPE_MISSING_DATA;
        }
    }

    @Override
    public void onBindViewHolder(@NonNull ClearCommInternalAdapter.ClearCommHolder holder, int position) {

        final DataAccessDescriptor currentDataAccessDescriptor = getDataAccessDescriptors().get(position);

        if (getItemViewType(position) == TYPE_REGULAR) {
            String sharingDescription = "";


            if(currentDataAccessDescriptor.isSharedWithAppCompany() && currentDataAccessDescriptor.isSharedWithThirdPartyCompany()) {
                sharingDescription += " - shared with our servers and 3rd party companies.";
            }
            else if(currentDataAccessDescriptor.isSharedWithAppCompany()) {
                sharingDescription += " - shared with our servers.";
            }
            else if(currentDataAccessDescriptor.isSharedWithThirdPartyCompany()) {
                sharingDescription += " - shared with 3rd party companies.";
            }
            else {
                sharingDescription += " - shared with no one.";
            }

            holder.descriptionOfDataAccessed.setText(currentDataAccessDescriptor.getDescriptionOfDataAccessed() + sharingDescription);

            holder.setItemClickListener(new ClearCommInternalAdapter.ItemClickListener() {
                @Override
                public void onItemclickListener(View v, int position) {
                    ClearCommEndUserPrivacyUI.ClearCommDetailedPopUp popUpClass = new ClearCommEndUserPrivacyUI.ClearCommDetailedPopUp(currentDataAccessDescriptor.getDescriptionOfDataAccessed(),
                            currentDataAccessDescriptor.getReasoningBehindDataAccession(),
                            currentDataAccessDescriptor.getReasonSharedWithAppCompany(),
                            currentDataAccessDescriptor.getReasonSharedWithThirdPartyCompany(),
                            currentDataAccessDescriptor.isDataAccessedDuringBackgroundUsage(),
                            currentDataAccessDescriptor.isSharedWithAppCompany(),
                            currentDataAccessDescriptor.isSharedWithThirdPartyCompany());
                    popUpClass.showPopupWindow(v);
                }
            });
        } else if(getItemViewType(position) == TYPE_MISSING_DATA) {
            holder.descriptionOfDataAccessed.setTextColor(Color.parseColor("#D82010"));
            holder.descriptionOfDataAccessed.setText(getDataAccessDescriptors().get(position).getDescriptionOfDataAccessed() + " - " + getDataAccessDescriptors().get(position).getErrorString());

            holder.setItemClickListener(new ClearCommInternalAdapter.ItemClickListener() {
                @Override
                public void onItemclickListener(View v, int position) {
                    System.out.println("Nah Dog");
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return getDataAccessDescriptors().size();
    }

    /**
     * This interface is used to handle clicking actions
     */
    public interface ItemClickListener {

        void onItemclickListener(View v, int position);
    }

    /**
     * The ClearCommHolder is used to create the internal attributes of each card
     */
    public static class ClearCommHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        TextView descriptionOfDataAccessed;
        ClearCommInternalAdapter.ItemClickListener itemClickListener;


        public ClearCommHolder(@NonNull View itemView) {
            super(itemView);
            this.descriptionOfDataAccessed = itemView.findViewById(R.id.clear_comm_data_detail_instance);

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            this.itemClickListener.onItemclickListener(v, getLayoutPosition());
        }

        public void setItemClickListener(ClearCommInternalAdapter.ItemClickListener ic) {
            this.itemClickListener = ic;
        }
    }
}