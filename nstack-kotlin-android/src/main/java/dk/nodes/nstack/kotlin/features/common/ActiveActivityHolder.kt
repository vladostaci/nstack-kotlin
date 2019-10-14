package dk.nodes.nstack.kotlin.features.common

import android.app.Activity
import android.app.Application
import android.os.Bundle
import java.lang.ref.WeakReference


/**
 * Used to access active activities, needs to be bound to the application context
 */
internal class ActiveActivityHolder : Application.ActivityLifecycleCallbacks {

    /**
     * If there is a foreground activity and it is resumed, this will be true. Otherwise it's false
     * for all other cases.
     */
    @Suppress("MemberVisibilityCanBePrivate")
    var isInForeground: Boolean = false
        private set

    var foregroundActivity: Activity?
        get() = foregroundActivityReference.get()
        private set(value) {
            throw UnsupportedOperationException(
                    "The foreground activity property should never be explicitly set! " +
                    "Use foregroundActivityReference to hold on to the activity."
            )
        }

    private var foregroundActivityReference: WeakReference<Activity?> = WeakReference(null)

    override fun onActivityPaused(activity: Activity) {
        if (foregroundActivity != null && foregroundActivity === activity) {
            isInForeground = false
        }
    }

    override fun onActivityStarted(activity: Activity) {
        foregroundActivityReference = WeakReference(activity)
    }

    override fun onActivityStopped(activity: Activity) {
        if (foregroundActivity != null && foregroundActivity === activity) {
            foregroundActivityReference = WeakReference(null)
        }
    }

    override fun onActivityResumed(activity: Activity) {
        isInForeground = true
    }

    override fun onActivityCreated(activity: Activity, savedInstanceState: Bundle?) {
        // Do nothing, it's not shown yet
    }

    override fun onActivitySaveInstanceState(activity: Activity, outState: Bundle) {
        // Do nothing, we only care about holding the foreground activities and have no interest in the instance state
    }

    override fun onActivityDestroyed(activity: Activity) {
        // Do nothing, should be already removed from the foregroundActivity variable at this point
    }
}