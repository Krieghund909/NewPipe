package org.schabi.newpipe.util;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.annotation.StringRes;
import androidx.fragment.app.Fragment;
import androidx.preference.PreferenceManager;

import org.schabi.newpipe.R;
import org.schabi.newpipe.extractor.stream.StreamInfoItem;
import org.schabi.newpipe.extractor.stream.StreamType;

public class StreamDialogEntry {

    @StringRes
    public final int resource;
    @NonNull
    public final StreamDialogEntryAction action;

    public StreamDialogEntry(@StringRes final int resource,
                             @NonNull final StreamDialogEntryAction action) {
        this.resource = resource;
        this.action = action;
    }

    public String getString(@NonNull final Context context) {
        return context.getString(resource);
    }

    public static boolean shouldAddMarkAsWatched(final StreamType streamType,
                                                 final Context context) {
        final boolean isWatchHistoryEnabled = PreferenceManager
                .getDefaultSharedPreferences(context)
                .getBoolean(context.getString(R.string.enable_watch_history_key), false);
        return streamType != StreamType.AUDIO_LIVE_STREAM
                && streamType != StreamType.LIVE_STREAM
                && isWatchHistoryEnabled;
    }

    public interface StreamDialogEntryAction {
        void onClick(Fragment fragment, StreamInfoItem infoItem);
    }
}
