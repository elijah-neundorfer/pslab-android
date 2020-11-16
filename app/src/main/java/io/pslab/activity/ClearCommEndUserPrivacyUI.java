package io.pslab.activity;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import io.pslab.R;

public class ClearCommEndUserPrivacyUI extends AppCompatActivity {

    /**
     README - INSTRUCTIONS
     The UI contains a button to navigate the user to your privacy policy. The below string
     determines the online location of your privacy policy. For example, you may change the string
     to "https://www.example.com/privacy_policy" Please update the string to reference your online
     privacy policy.

     Please note that the String must be a valid url and begin with 'http://' or 'https://' or we
     cannot navigate to the link

     If you do not have a privacy policy, please initialize this variable as an empty string - ""
     If initialized as an empty string, the privacy policy button will not appear in the UI

     */
    final String privacyPolicyURL = "https://pslab.io/privacy-policy/";


    /* DO NOT EDIT ANY CODE BELOW THIS LINE */


    RecyclerView dataAccessEntriesRecyclerView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.clear_comm_end_user_privacy_ui);

        dataAccessEntriesRecyclerView = findViewById(R.id.dataAccessEntries);
        dataAccessEntriesRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        dataAccessEntriesRecyclerView.setAdapter(new ClearCommAdapter(this));

        Button systemPermissionSettingsButton = findViewById(R.id.SystemPermissionSettings);
        Button privacyPolicyButton = findViewById(R.id.PrivacyPolicy);

        systemPermissionSettingsButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                // Start new activity
                Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                Uri uri = Uri.fromParts("package", getPackageName(), null);
                intent.setData(uri);
                startActivity(intent);
            }
        });
        if(privacyPolicyURL != null && !privacyPolicyURL.trim().isEmpty()) {
            privacyPolicyButton.setVisibility(View.VISIBLE);
            privacyPolicyButton.setOnClickListener(new View.OnClickListener() {
                public void onClick(View view) {
                    Uri uri;
                    try {
                        uri = Uri.parse(privacyPolicyURL);
                        Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                        startActivity(intent);
                    } catch (ActivityNotFoundException e) {
                        Toast.makeText(getApplicationContext(),"Oops, we cannot find that privacy policy link",Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
    }

    public static class ClearCommDetailedPopUp {

        String dataDescription, dataAccessReasoning, reasonDataSharedWithAppCompany, reasonDataSharedWithThirdPartyCompany;
        boolean isDataAccessedDuringBackgroundUsage, isDataSharedWithAppCompany, isDataSharedWithThirdPartyCompany;

        public ClearCommDetailedPopUp(String dataDescription, String dataAccessReasoning, String reasonDataSharedWithAppCompany, String reasonDataSharedWithThirdPartyCompany, boolean isDataAccessedDuringBackgroundUsage, boolean isDataSharedWithAppCompany, boolean isDataSharedWithThirdPartyCompany) {
            this.dataDescription = dataDescription;
            this.dataAccessReasoning = dataAccessReasoning;
            this.reasonDataSharedWithAppCompany = reasonDataSharedWithAppCompany;
            this.reasonDataSharedWithThirdPartyCompany = reasonDataSharedWithThirdPartyCompany;
            this.isDataAccessedDuringBackgroundUsage = isDataAccessedDuringBackgroundUsage;
            this.isDataSharedWithAppCompany = isDataSharedWithAppCompany;
            this.isDataSharedWithThirdPartyCompany = isDataSharedWithThirdPartyCompany;
        }

        public void showPopupWindow(final View view) {

            //Create a View object yourself through inflater
            LayoutInflater inflater = (LayoutInflater) view.getContext().getSystemService(view.getContext().LAYOUT_INFLATER_SERVICE);
            View popupView = inflater.inflate(R.layout.clear_comm_detailed_pop_up, null);

            //Specify the length and width through constants
            int width = LinearLayout.LayoutParams.WRAP_CONTENT;
            int height = LinearLayout.LayoutParams.WRAP_CONTENT;


            //Make Inactive Items Outside Of PopupWindow
            boolean focusable = true;

            //Create a window with our parameters
            final PopupWindow popupWindow = new PopupWindow(popupView, width, height, focusable);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                popupWindow.setElevation(20);
            }


            //Set the location of the window on the screen
            popupWindow.showAtLocation(view, Gravity.CENTER, 0, 0);

            //Initialize the elements of our window, install the handler

            TextView dataAccessTitle = popupView.findViewById(R.id.dataAccessTitle);
            dataAccessTitle.setText(dataDescription);

            String totalDataAccessReasoning = "We access your " + dataDescription + " to " + dataAccessReasoning;
            TextView dataAccessReasoningView = popupView.findViewById(R.id.dataAccessReasoning);
            dataAccessReasoningView.setText(totalDataAccessReasoning);

            String totalSharingDescription = "Your " + dataDescription + " is shared with ";

            if(isDataSharedWithAppCompany && isDataSharedWithThirdPartyCompany) {
                totalSharingDescription += "the app company's server in order to " + reasonDataSharedWithAppCompany + " and is shared with 3rd party companies in order to " + reasonDataSharedWithThirdPartyCompany + ".";
            }
            else if(isDataSharedWithAppCompany) {
                totalSharingDescription += "the app company's server in order to " + reasonDataSharedWithAppCompany + ".";
            }
            else if(isDataSharedWithThirdPartyCompany) {
                totalSharingDescription += "3rd party companies in order to " + reasonDataSharedWithThirdPartyCompany + ".";
            }
            else {
                totalSharingDescription += "no one.";
            }

            TextView dataSharingDescriptionView = popupView.findViewById(R.id.dataSharingDescription);
            dataSharingDescriptionView.setText(totalSharingDescription);

            TextView dataBackgroundAccessDescriptionView = popupView.findViewById(R.id.dataBackgroundAccessDescription);
            if(isDataAccessedDuringBackgroundUsage) {
                dataBackgroundAccessDescriptionView.setText("The app may access your " + dataDescription + " while the app is not in use.");
            }
            else {
                dataBackgroundAccessDescriptionView.setText("The app will never access your " + dataDescription + " while the app is not in use.");
            }
        }
    }
}