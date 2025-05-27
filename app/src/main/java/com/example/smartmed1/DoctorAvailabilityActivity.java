package com.example.smartmed1;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.content.Intent;
import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import android.app.AlertDialog;
import android.content.DialogInterface;

public class DoctorAvailabilityActivity extends AppCompatActivity {

    private static final int ADD_AVAILABILITY_REQUEST = 1;

    // Declare the ActivityResultLauncher
    private ActivityResultLauncher<Intent> addAvailabilityLauncher;

    // Member variables to keep track of the selected availability entry
    private TextView selectedStartTimeTextView = null;
    private TextView selectedEndTimeTextView = null;
    private String selectedDayForEdit = null;
    private int selectedIndexForEdit = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_doctor_availability);

        // Initialize ActivityResultLauncher
        addAvailabilityLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                new ActivityResultCallback<ActivityResult>() {
                    @Override
                    public void onActivityResult(ActivityResult result) {
                        if (result.getResultCode() == RESULT_OK) {
                            Intent data = result.getData();
                            if (data != null) {
                                String selectedDay = data.getStringExtra("selectedDay");
                                String selectedStartTime = data.getStringExtra("selectedStartTime");
                                String selectedEndTime = data.getStringExtra("selectedEndTime");

                                // Check if this is an edit operation
                                if (data.hasExtra("originalIndex") && data.hasExtra("originalDay")) {
                                    int originalIndex = data.getIntExtra("originalIndex", -1);
                                    String originalDay = data.getStringExtra("originalDay");

                                    // If the day was changed during edit, remove the old entry
                                    if (!selectedDay.equals(originalDay)) {
                                        LinearLayout originalStartLayout = findLinearLayoutForDay(originalDay, true);
                                        LinearLayout originalEndLayout = findLinearLayoutForDay(originalDay, false);
                                        if (originalStartLayout != null && originalStartLayout.getChildCount() > originalIndex) {
                                            originalStartLayout.removeViewAt(originalIndex);
                                            originalEndLayout.removeViewAt(originalIndex);
                                        }

                                        // Add the new entry to the new day
                                        LinearLayout newStartLayout = findLinearLayoutForDay(selectedDay, true);
                                        LinearLayout newEndLayout = findLinearLayoutForDay(selectedDay, false);
                                        if (newStartLayout != null && newEndLayout != null) {
                                            addTimeTextView(newStartLayout, selectedStartTime);
                                            addTimeTextView(newEndLayout, selectedEndTime);
                                        }

                                    } else { // Day was not changed, just update the existing entry
                                        LinearLayout startLayout = findLinearLayoutForDay(selectedDay, true);
                                        LinearLayout endLayout = findLinearLayoutForDay(selectedDay, false);

                                        if (startLayout != null && startLayout.getChildCount() > originalIndex) {
                                            // Update the TextViews at the original index
                                            TextView startTextView = (TextView) startLayout.getChildAt(originalIndex);
                                            TextView endTextView = (TextView) endLayout.getChildAt(originalIndex);
                                            startTextView.setText(selectedStartTime);
                                            endTextView.setText(selectedEndTime);
                                        }
                                    }
                                } else {
                                    // This is an add operation (existing logic)
                                    updateAvailabilityUI(selectedDay, selectedStartTime, selectedEndTime);
                                }
                            }
                        }
                    }
                });

        // Get references to the LinearLayouts for each day
        LinearLayout mondayStartTimesLayout = findViewById(R.id.linearLayoutMondayStartTimes);
        LinearLayout mondayEndTimesLayout = findViewById(R.id.linearLayoutMondayEndTimes);
        LinearLayout tuesdayStartTimesLayout = findViewById(R.id.linearLayoutTuesdayStartTimes);
        LinearLayout tuesdayEndTimesLayout = findViewById(R.id.linearLayoutTuesdayEndTimes);
        LinearLayout wednesdayStartTimesLayout = findViewById(R.id.linearLayoutWednesdayStartTimes);
        LinearLayout wednesdayEndTimesLayout = findViewById(R.id.linearLayoutWednesdayEndTimes);
        LinearLayout thursdayStartTimesLayout = findViewById(R.id.linearLayoutThursdayStartTimes);
        LinearLayout thursdayEndTimesLayout = findViewById(R.id.linearLayoutThursdayEndTimes);
        LinearLayout fridayStartTimesLayout = findViewById(R.id.linearLayoutFridayStartTimes);
        LinearLayout fridayEndTimesLayout = findViewById(R.id.linearLayoutFridayEndTimes);
        LinearLayout saturdayStartTimesLayout = findViewById(R.id.linearLayoutSaturdayStartTimes);
        LinearLayout saturdayEndTimesLayout = findViewById(R.id.linearLayoutSaturdayEndTimes);
        LinearLayout sundayStartTimesLayout = findViewById(R.id.linearLayoutSundayStartTimes);
        LinearLayout sundayEndTimesLayout = findViewById(R.id.linearLayoutSundayEndTimes);

        // Add placeholder times for each day
        addTimeTextView(mondayStartTimesLayout, "09:00");
        addTimeTextView(mondayStartTimesLayout, "10:00");
        addTimeTextView(mondayEndTimesLayout, "13:00");
        addTimeTextView(mondayEndTimesLayout, "14:00");

        addTimeTextView(tuesdayStartTimesLayout, "14:00");
        addTimeTextView(tuesdayEndTimesLayout, "17:00");

        addTimeTextView(wednesdayStartTimesLayout, "10:00");
        addTimeTextView(wednesdayEndTimesLayout, "13:00");

        addTimeTextView(thursdayStartTimesLayout, "09:30");
        addTimeTextView(thursdayEndTimesLayout, "12:30");
        addTimeTextView(thursdayStartTimesLayout, "15:00");
        addTimeTextView(thursdayEndTimesLayout, "18:00");

        addTimeTextView(fridayStartTimesLayout, "08:00");
        addTimeTextView(fridayEndTimesLayout, "12:00");

        // Add hours for Saturday
        addTimeTextView(saturdayStartTimesLayout, "10:00");
        addTimeTextView(saturdayEndTimesLayout, "14:00");

        // Add hours for Sunday
        addTimeTextView(sundayStartTimesLayout, "11:00");
        addTimeTextView(sundayEndTimesLayout, "15:00");

        // Get reference to the Modify Availability button
        Button modifyButton = findViewById(R.id.buttonModify);
        // Get reference to the Add Availability button
        Button addButton = findViewById(R.id.buttonAdd);

        // Set OnClickListener for the Add Availability button
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Deselect any currently selected item before adding a new one
                clearSelection();
                Intent intent = new Intent(DoctorAvailabilityActivity.this, AddEditAvailabilityActivity.class);
                addAvailabilityLauncher.launch(intent);
            }
        });

        // Set OnClickListener for the Modify Availability button
        modifyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Check if an entry is selected for editing
                if (selectedStartTimeTextView != null && selectedEndTimeTextView != null && selectedDayForEdit != null && selectedIndexForEdit != -1) {
                    Intent intent = new Intent(DoctorAvailabilityActivity.this, AddEditAvailabilityActivity.class);
                    intent.putExtra("is_edit", true);
                    intent.putExtra("day", selectedDayForEdit);
                    intent.putExtra("startTime", selectedStartTimeTextView.getText().toString());
                    intent.putExtra("endTime", selectedEndTimeTextView.getText().toString());
                    intent.putExtra("index", selectedIndexForEdit);
                    intent.putExtra("originalDay", selectedDayForEdit);
                    addAvailabilityLauncher.launch(intent);
                } else {
                    // Show a message to select an entry first
                    // TODO: Use a Toast or Snackbar for feedback
                    // Toast.makeText(DoctorAvailabilityActivity.this, "Παρακαλώ επιλέξτε μια διαθεσιμότητα για τροποποίηση.", Toast.LENGTH_SHORT).show();
                }
            }
        });


    }

    // Helper method to add a TextView for a time entry
    private void addTimeTextView(LinearLayout parentLayout, String time) {
        TextView timeTextView = new TextView(this);
        timeTextView.setLayoutParams(new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT));
        timeTextView.setText(time);
        timeTextView.setTextSize(14);
        timeTextView.setTextColor(getResources().getColor(android.R.color.black));
        timeTextView.setGravity(Gravity.CENTER);
        timeTextView.setBackgroundResource(android.R.color.white); // Add a background to visualize
        timeTextView.setPadding(8, 8, 8, 8);
        parentLayout.addView(timeTextView);



        // Add OnClickListener for selection
        timeTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Handle selection
                handleAvailabilitySelection((TextView) v);
            }
        });

        // Add OnLongClickListener for deletion
        timeTextView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                // Pass the parent layout, the TextView, and its index
                LinearLayout parent = (LinearLayout) v.getParent();
                int index = parent.indexOfChild(v);
                showDeleteConfirmationDialog(parent, (TextView) v, time, index);
                return true; // Consume the long click event
            }
        });
    }

    // Method to handle selection of an availability entry
    private void handleAvailabilitySelection(TextView tappedTextView) {
        LinearLayout parentLayout = (LinearLayout) tappedTextView.getParent();
        int index = parentLayout.indexOfChild(tappedTextView);

        LinearLayout dayRowLayout = (LinearLayout) parentLayout.getParent();
        LinearLayout startTimesLayout = null;
        LinearLayout endTimesLayout = null;
        String currentDay = "";

        // Determine the day and get the start/end layout pair
        if (dayRowLayout.findViewById(R.id.linearLayoutMondayStartTimes) == parentLayout || dayRowLayout.findViewById(R.id.linearLayoutMondayEndTimes) == parentLayout) {
            currentDay = "Δευτέρα";
            startTimesLayout = dayRowLayout.findViewById(R.id.linearLayoutMondayStartTimes);
            endTimesLayout = dayRowLayout.findViewById(R.id.linearLayoutMondayEndTimes);
        } else if (dayRowLayout.findViewById(R.id.linearLayoutTuesdayStartTimes) == parentLayout || dayRowLayout.findViewById(R.id.linearLayoutTuesdayEndTimes) == parentLayout) {
            currentDay = "Τρίτη";
            startTimesLayout = dayRowLayout.findViewById(R.id.linearLayoutTuesdayStartTimes);
            endTimesLayout = dayRowLayout.findViewById(R.id.linearLayoutTuesdayEndTimes);
        } else if (dayRowLayout.findViewById(R.id.linearLayoutWednesdayStartTimes) == parentLayout || dayRowLayout.findViewById(R.id.linearLayoutWednesdayEndTimes) == parentLayout) {
            currentDay = "Τετάρτη";
            startTimesLayout = dayRowLayout.findViewById(R.id.linearLayoutWednesdayStartTimes);
            endTimesLayout = dayRowLayout.findViewById(R.id.linearLayoutWednesdayEndTimes);
        } else if (dayRowLayout.findViewById(R.id.linearLayoutThursdayStartTimes) == parentLayout || dayRowLayout.findViewById(R.id.linearLayoutThursdayEndTimes) == parentLayout) {
            currentDay = "Πέμπτη";
            startTimesLayout = dayRowLayout.findViewById(R.id.linearLayoutThursdayStartTimes);
            endTimesLayout = dayRowLayout.findViewById(R.id.linearLayoutThursdayEndTimes);
        } else if (dayRowLayout.findViewById(R.id.linearLayoutFridayStartTimes) == parentLayout || dayRowLayout.findViewById(R.id.linearLayoutFridayEndTimes) == parentLayout) {
            currentDay = "Παρασκευή";
            startTimesLayout = dayRowLayout.findViewById(R.id.linearLayoutFridayStartTimes);
            endTimesLayout = dayRowLayout.findViewById(R.id.linearLayoutFridayEndTimes);
        } else if (dayRowLayout.findViewById(R.id.linearLayoutSaturdayStartTimes) == parentLayout || dayRowLayout.findViewById(R.id.linearLayoutSaturdayEndTimes) == parentLayout) {
            currentDay = "Σάββατο";
            startTimesLayout = dayRowLayout.findViewById(R.id.linearLayoutSaturdayStartTimes);
            endTimesLayout = dayRowLayout.findViewById(R.id.linearLayoutSaturdayEndTimes);
        } else if (dayRowLayout.findViewById(R.id.linearLayoutSundayStartTimes) == parentLayout || dayRowLayout.findViewById(R.id.linearLayoutSundayEndTimes) == parentLayout) {
            currentDay = "Κυριακή";
            startTimesLayout = dayRowLayout.findViewById(R.id.linearLayoutSundayStartTimes);
            endTimesLayout = dayRowLayout.findViewById(R.id.linearLayoutSundayEndTimes);
        }

        if (startTimesLayout != null && endTimesLayout != null && startTimesLayout.getChildCount() > index) {
            // Deselect previously selected entry
            if (selectedStartTimeTextView != null && selectedEndTimeTextView != null) {
                selectedStartTimeTextView.setBackgroundResource(android.R.color.white); // Or your default background
                selectedEndTimeTextView.setBackgroundResource(android.R.color.white); // Or your default background
            }

            // Select the tapped entry
            selectedStartTimeTextView = (TextView) startTimesLayout.getChildAt(index);
            selectedEndTimeTextView = (TextView) endTimesLayout.getChildAt(index);
            selectedDayForEdit = currentDay;
            selectedIndexForEdit = index;

            // Highlight the selected entry (use a different color or drawable)
            selectedStartTimeTextView.setBackgroundColor(getResources().getColor(R.color.selected_item_background)); // Replace with your color resource
            selectedEndTimeTextView.setBackgroundColor(getResources().getColor(R.color.selected_item_background)); // Replace with your color resource
        }
    }

    // Method to clear the current selection
    private void clearSelection() {
        if (selectedStartTimeTextView != null && selectedEndTimeTextView != null) {
            selectedStartTimeTextView.setBackgroundResource(android.R.color.white); // Or your default background
            selectedEndTimeTextView.setBackgroundResource(android.R.color.white); // Or your default background
            selectedStartTimeTextView = null;
            selectedEndTimeTextView = null;
            selectedDayForEdit = null;
            selectedIndexForEdit = -1;
        }
    }

    // Method to show a confirmation dialog for deleting a time entry pair
    private void showDeleteConfirmationDialog(final LinearLayout parentLayout, final TextView timeTextView, String time, final int index) {
        new AlertDialog.Builder(this)
                .setTitle("Διαγραφή Διαθεσιμότητας")
                .setMessage("Είστε σίγουροι ότι θέλετε να διαγράψετε την καταχώρηση " + time + " και την αντίστοιχη ώρα;")
                .setPositiveButton("Διαγραφή", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // Get the parent of the parent (the daily row LinearLayout)
                        LinearLayout dayRowLayout = (LinearLayout) parentLayout.getParent();
                        LinearLayout correspondingLayout = null;

                        // Determine if the current parent is the start or end time layout
                        boolean isStartTimeLayout = false;
                        if (parentLayout.getId() == R.id.linearLayoutMondayStartTimes ||
                            parentLayout.getId() == R.id.linearLayoutTuesdayStartTimes ||
                            parentLayout.getId() == R.id.linearLayoutWednesdayStartTimes ||
                            parentLayout.getId() == R.id.linearLayoutThursdayStartTimes ||
                            parentLayout.getId() == R.id.linearLayoutFridayStartTimes ||
                            parentLayout.getId() == R.id.linearLayoutSaturdayStartTimes ||
                            parentLayout.getId() == R.id.linearLayoutSundayStartTimes) {
                            isStartTimeLayout = true;
                        }

                        // Find the corresponding layout within the day row
                        if (isStartTimeLayout) {
                            // If current is start, find the end time layout
                            if (parentLayout.getId() == R.id.linearLayoutMondayStartTimes) correspondingLayout = dayRowLayout.findViewById(R.id.linearLayoutMondayEndTimes);
                            else if (parentLayout.getId() == R.id.linearLayoutTuesdayStartTimes) correspondingLayout = dayRowLayout.findViewById(R.id.linearLayoutTuesdayEndTimes);
                            else if (parentLayout.getId() == R.id.linearLayoutWednesdayStartTimes) correspondingLayout = dayRowLayout.findViewById(R.id.linearLayoutWednesdayEndTimes);
                            else if (parentLayout.getId() == R.id.linearLayoutThursdayStartTimes) correspondingLayout = dayRowLayout.findViewById(R.id.linearLayoutThursdayEndTimes);
                            else if (parentLayout.getId() == R.id.linearLayoutFridayStartTimes) correspondingLayout = dayRowLayout.findViewById(R.id.linearLayoutFridayEndTimes);
                            else if (parentLayout.getId() == R.id.linearLayoutSaturdayStartTimes) correspondingLayout = dayRowLayout.findViewById(R.id.linearLayoutSaturdayEndTimes);
                            else if (parentLayout.getId() == R.id.linearLayoutSundayStartTimes) correspondingLayout = dayRowLayout.findViewById(R.id.linearLayoutSundayEndTimes);
                        } else {
                            // If current is end, find the start time layout
                            if (parentLayout.getId() == R.id.linearLayoutMondayEndTimes) correspondingLayout = dayRowLayout.findViewById(R.id.linearLayoutMondayStartTimes);
                            else if (parentLayout.getId() == R.id.linearLayoutTuesdayEndTimes) correspondingLayout = dayRowLayout.findViewById(R.id.linearLayoutTuesdayStartTimes);
                            else if (parentLayout.getId() == R.id.linearLayoutWednesdayEndTimes) correspondingLayout = dayRowLayout.findViewById(R.id.linearLayoutWednesdayStartTimes);
                            else if (parentLayout.getId() == R.id.linearLayoutThursdayEndTimes) correspondingLayout = dayRowLayout.findViewById(R.id.linearLayoutThursdayStartTimes);
                            else if (parentLayout.getId() == R.id.linearLayoutFridayEndTimes) correspondingLayout = dayRowLayout.findViewById(R.id.linearLayoutFridayStartTimes);
                            else if (parentLayout.getId() == R.id.linearLayoutSaturdayEndTimes) correspondingLayout = dayRowLayout.findViewById(R.id.linearLayoutSaturdayStartTimes);
                            else if (parentLayout.getId() == R.id.linearLayoutSundayEndTimes) correspondingLayout = dayRowLayout.findViewById(R.id.linearLayoutSundayStartTimes);
                        }

                        // Remove the selected TextView and its corresponding TextView if found and index is valid
                        if (correspondingLayout != null && correspondingLayout.getChildCount() > index) {
                            parentLayout.removeViewAt(index);
                            correspondingLayout.removeViewAt(index);
                        } else {
                            // Fallback: just remove the selected one if the corresponding couldn't be found or indices don't match (shouldn't happen with correct logic)
                            parentLayout.removeView(timeTextView);
                        }
                    }
                })
                .setNegativeButton("Ακύρωση", null) // Dismiss the dialog on cancel
                .setIcon(android.R.drawable.ic_dialog_alert)
                .show();
    }

    // Method to update the UI with selected availability
    private void updateAvailabilityUI(String day, String startTime, String endTime) {
        // TODO: Implement logic to find the correct LinearLayout and update or add TextViews
        // This is a placeholder for the actual update logic
        // You might need to clear existing time views for the day before adding new ones
        LinearLayout startTimesLayout = findLinearLayoutForDay(day, true);
        LinearLayout endTimesLayout = findLinearLayoutForDay(day, false);

        if (startTimesLayout != null && endTimesLayout != null) {
            // Clear existing times for the selected day (assuming replacement)
            startTimesLayout.removeAllViews();
            endTimesLayout.removeAllViews();

            // Add the new selected time range
            addTimeTextView(startTimesLayout, startTime);
            addTimeTextView(endTimesLayout, endTime);
        }
    }

    // Helper method to find the LinearLayout for a specific day
    private LinearLayout findLinearLayoutForDay(String day, boolean isStartTime) {
        switch (day) {
            case "Δευτέρα":
                return findViewById(isStartTime ? R.id.linearLayoutMondayStartTimes : R.id.linearLayoutMondayEndTimes);
            case "Τρίτη":
                return findViewById(isStartTime ? R.id.linearLayoutTuesdayStartTimes : R.id.linearLayoutTuesdayEndTimes);
            case "Τετάρτη":
                return findViewById(isStartTime ? R.id.linearLayoutWednesdayStartTimes : R.id.linearLayoutWednesdayEndTimes);
            case "Πέμπτη":
                return findViewById(isStartTime ? R.id.linearLayoutThursdayStartTimes : R.id.linearLayoutThursdayEndTimes);
            case "Παρασκευή":
                return findViewById(isStartTime ? R.id.linearLayoutFridayStartTimes : R.id.linearLayoutFridayEndTimes);
            case "Σάββατο":
                return findViewById(isStartTime ? R.id.linearLayoutSaturdayStartTimes : R.id.linearLayoutSaturdayEndTimes);
            case "Κυριακή":
                return findViewById(isStartTime ? R.id.linearLayoutSundayStartTimes : R.id.linearLayoutSundayEndTimes);
            default:
                return null;
        }
    }
} 