package com.avelycure.spotifyclone.exoplayer

import com.avelycure.spotifyclone.exoplayer.State.*

/**
 * In this class we will have a list of songs that we got from firebase.
 * This will take time to get song from firestore. But we can just wait in our music service.
 * So we create State variable to check it.
 */
class FirebaseMusicSource {



    private val onReadyListeners = mutableListOf<(Boolean) -> Unit>()

    private var state: State = STATE_CREATED
        set(value) {
            if (value == STATE_INITIALIZED || value == STATE_ERROR) {
                synchronized(onReadyListeners) {
                    field = value
                    onReadyListeners.forEach { listener ->
                        listener(state == STATE_INITIALIZED)
                    }
                }
            } else {
                field = value
            }
        }

    fun whenReady(action: (Boolean) -> Unit):Boolean{
        if(state == STATE_CREATED || state == STATE_INITIALIZING){
            //music source is not ready
            onReadyListeners += action
            return false
        }else{
            //music source is ready
            action(state==STATE_INITIALIZED)
            return true
        }
    }
}

enum class State {
    STATE_CREATED,
    STATE_INITIALIZING,
    STATE_INITIALIZED,
    STATE_ERROR
}