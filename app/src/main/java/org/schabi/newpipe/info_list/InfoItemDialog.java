package org.schabi.newpipe.info_list;

import android.app.Activity;
import android.content.DialogInterface;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import org.schabi.newpipe.R;
import org.schabi.newpipe.extractor.stream.StreamInfoItem;
import org.schabi.newpipe.util.StreamDialogDefaultEntry;
import org.schabi.newpipe.util.StreamDialogEntry;

import java.util.ArrayList;
import java.util.List;

/**
 * Dialog with actions for a {@link StreamInfoItem}.
 * This dialog is mostly used for longpress context menus.
 */
public final class InfoItemDialog {
    private final AlertDialog dialog;

    private InfoItemDialog(@NonNull final Activity activity,
                          @NonNull final Fragment fragment,
                          @NonNull final StreamInfoItem info,
                          @NonNull final List<StreamDialogEntry> entries) {

        final View bannerView = View.inflate(activity, R.layout.dialog_title, null);
        bannerView.setSelected(true);

        final TextView titleView = bannerView.findViewById(R.id.itemTitleView);
        titleView.setText(info.getName());

        final TextView detailsView = bannerView.findViewById(R.id.itemAdditionalDetails);
        if (info.getUploaderName() != null) {
            detailsView.setText(info.getUploaderName());
            detailsView.setVisibility(View.VISIBLE);
        } else {
            detailsView.setVisibility(View.GONE);
        }

        final String[] items = entries.stream()
                .map(entry -> entry.getString(activity)).toArray(String[]::new);

        final DialogInterface.OnClickListener action = (d, index) ->
            entries.get(index).action.onClick(fragment, info);

        dialog = new AlertDialog.Builder(activity)
                .setCustomTitle(bannerView)
                .setItems(items, action)
                .create();

    }

    public void show() {
        dialog.show();
    }

    /**
     * <p>Builder to generate a {@link InfoItemDialog}.</p>
     * Use {@link #addEntry(StreamDialogDefaultEntry)}
     * and {@link #addAllEntries(StreamDialogDefaultEntry...)} to add options to the dialog.
     * <br>
     * Custom actions for entries can be set using
     * {@link #addEntry(StreamDialogDefaultEntry, StreamDialogEntry.StreamDialogEntryAction)} and
     * {@link #setAction(StreamDialogDefaultEntry, StreamDialogEntry.StreamDialogEntryAction)}.
     */
    public static class Builder {
        @NonNull private final Activity activity;
        @NonNull private final StreamInfoItem info;
        @NonNull private final Fragment fragment;
        @NonNull private final List<StreamDialogEntry> entries = new ArrayList<>();

        public Builder(@NonNull final Activity activity,
                       @NonNull final Fragment fragment,
                       @NonNull final StreamInfoItem info) {
            this.activity = activity;
            this.fragment = fragment;
            this.info = info;
        }

        public void addEntry(@NonNull final StreamDialogDefaultEntry entry) {
            entries.add(entry.toStreamDialogEntry());
        }

        public void addEntry(@NonNull final StreamDialogDefaultEntry entry,
                             @NonNull final StreamDialogEntry.StreamDialogEntryAction action) {
            entries.add(new StreamDialogEntry(entry.resource, action));
        }

        public void addAllEntries(@NonNull final StreamDialogDefaultEntry... newEntries) {
            for (final StreamDialogDefaultEntry entry: newEntries) {
                this.entries.add(entry.toStreamDialogEntry());
            }
        }

        public void setAction(@NonNull final StreamDialogDefaultEntry entry,
                              @NonNull final StreamDialogEntry.StreamDialogEntryAction action) {
            for (int i = 0; i < entries.size(); i++) {
                if (entries.get(i).resource == entry.resource) {
                    entries.set(i, new StreamDialogEntry(entry.resource, action));
                }
            }
        }

        public InfoItemDialog build() {
            return new InfoItemDialog(this.activity, this.fragment, this.info, this.entries);
        }
    }
}
