package io.github.chrislo27.rhre3.playalong

import com.badlogic.gdx.controllers.Controller
import com.badlogic.gdx.controllers.ControllerListener
import com.badlogic.gdx.controllers.PovDirection
import com.badlogic.gdx.math.Vector3
import io.github.chrislo27.toolboks.Toolboks
import java.util.*


class PlayalongControllerListener(val playalong: Playalong) : ControllerListener {

    private fun getMapping(controller: Controller): ControllerMapping? = Playalong.activeControllerMappings[controller]

    override fun connected(controller: Controller) {
        Toolboks.LOGGER.info("[PlayalongControllerListener] Controller ${controller.name} connected")
    }

    override fun disconnected(controller: Controller) {
        Toolboks.LOGGER.info("[PlayalongControllerListener] Controller ${controller.name} disconnected")
    }

    override fun buttonDown(controller: Controller, buttonCode: Int): Boolean {
        val mapping = getMapping(controller) ?: return false
        val buttonA = mapping.buttonA
        val buttonB = mapping.buttonB
        val buttonLeft = mapping.buttonLeft
        val buttonRight = mapping.buttonRight
        val buttonUp = mapping.buttonUp
        val buttonDown = mapping.buttonDown
        var any = false
        if (buttonA is ControllerInput.Button && buttonA.code == buttonCode) {
            playalong.handleInput(true, EnumSet.of(PlayalongInput.BUTTON_A, PlayalongInput.BUTTON_A_OR_DPAD), buttonCode shl 3, false)
            any = true
        }
        if (buttonB is ControllerInput.Button && buttonB.code == buttonCode) {
            playalong.handleInput(true, EnumSet.of(PlayalongInput.BUTTON_B), buttonCode shl 3, false)
            any = true
        }
        if (buttonLeft is ControllerInput.Button && buttonLeft.code == buttonCode) {
            playalong.handleInput(true, EnumSet.of(PlayalongInput.BUTTON_DPAD_LEFT, PlayalongInput.BUTTON_DPAD, PlayalongInput.BUTTON_A_OR_DPAD), buttonCode shl 3, false)
            any = true
        }
        if (buttonRight is ControllerInput.Button && buttonRight.code == buttonCode) {
            playalong.handleInput(true, EnumSet.of(PlayalongInput.BUTTON_DPAD_RIGHT, PlayalongInput.BUTTON_DPAD, PlayalongInput.BUTTON_A_OR_DPAD), buttonCode shl 3, false)
            any = true
        }
        if (buttonUp is ControllerInput.Button && buttonUp.code == buttonCode) {
            playalong.handleInput(true, EnumSet.of(PlayalongInput.BUTTON_DPAD_UP, PlayalongInput.BUTTON_DPAD, PlayalongInput.BUTTON_A_OR_DPAD), buttonCode shl 3, false)
            any = true
        }
        if (buttonDown is ControllerInput.Button && buttonDown.code == buttonCode) {
            playalong.handleInput(true, EnumSet.of(PlayalongInput.BUTTON_DPAD_DOWN, PlayalongInput.BUTTON_DPAD, PlayalongInput.BUTTON_A_OR_DPAD), buttonCode shl 3, false)
            any = true
        }
        return any
    }

    override fun buttonUp(controller: Controller, buttonCode: Int): Boolean {
        val mapping = getMapping(controller) ?: return false
        val buttonA = mapping.buttonA
        val buttonB = mapping.buttonB
        val buttonLeft = mapping.buttonLeft
        val buttonRight = mapping.buttonRight
        val buttonUp = mapping.buttonUp
        val buttonDown = mapping.buttonDown
        var any = false
        if (buttonA is ControllerInput.Button && buttonA.code == buttonCode) {
            playalong.handleInput(false, EnumSet.of(PlayalongInput.BUTTON_A, PlayalongInput.BUTTON_A_OR_DPAD), buttonCode shl 3, false)
            any = true
        }
        if (buttonB is ControllerInput.Button && buttonB.code == buttonCode) {
            playalong.handleInput(false, EnumSet.of(PlayalongInput.BUTTON_B), buttonCode shl 3, false)
            any = true
        }
        if (buttonLeft is ControllerInput.Button && buttonLeft.code == buttonCode) {
            playalong.handleInput(false, EnumSet.of(PlayalongInput.BUTTON_DPAD_LEFT, PlayalongInput.BUTTON_DPAD, PlayalongInput.BUTTON_A_OR_DPAD), buttonCode shl 3, false)
            any = true
        }
        if (buttonRight is ControllerInput.Button && buttonRight.code == buttonCode) {
            playalong.handleInput(false, EnumSet.of(PlayalongInput.BUTTON_DPAD_RIGHT, PlayalongInput.BUTTON_DPAD, PlayalongInput.BUTTON_A_OR_DPAD), buttonCode shl 3, false)
            any = true
        }
        if (buttonUp is ControllerInput.Button && buttonUp.code == buttonCode) {
            playalong.handleInput(false, EnumSet.of(PlayalongInput.BUTTON_DPAD_UP, PlayalongInput.BUTTON_DPAD, PlayalongInput.BUTTON_A_OR_DPAD), buttonCode shl 3, false)
            any = true
        }
        if (buttonDown is ControllerInput.Button && buttonDown.code == buttonCode) {
            playalong.handleInput(false, EnumSet.of(PlayalongInput.BUTTON_DPAD_DOWN, PlayalongInput.BUTTON_DPAD, PlayalongInput.BUTTON_A_OR_DPAD), buttonCode shl 3, false)
            any = true
        }
        return any
    }

    override fun povMoved(controller: Controller, povCode: Int, value: PovDirection): Boolean {
        val mapping = getMapping(controller) ?: return false
        val buttonA = mapping.buttonA
        val buttonB = mapping.buttonB
        val buttonLeft = mapping.buttonLeft
        val buttonRight = mapping.buttonRight
        val buttonUp = mapping.buttonUp
        val buttonDown = mapping.buttonDown
        var any = false
        val release = value == PovDirection.center
        if (buttonA is ControllerInput.Pov && buttonA.povCode == povCode && (buttonA.direction == value || release)) {
            playalong.handleInput(!release, EnumSet.of(PlayalongInput.BUTTON_A, PlayalongInput.BUTTON_A_OR_DPAD), value.ordinal shl (povCode + 2), false)
            any = true
        }
        if (buttonB is ControllerInput.Pov && buttonB.povCode == povCode && (buttonB.direction == value || release)) {
            playalong.handleInput(!release, EnumSet.of(PlayalongInput.BUTTON_B), value.ordinal shl (povCode + 2), false)
            any = true
        }
        if (buttonLeft is ControllerInput.Pov && buttonLeft.povCode == povCode && (buttonLeft.direction == value || release)) {
            playalong.handleInput(!release, EnumSet.of(PlayalongInput.BUTTON_DPAD_LEFT, PlayalongInput.BUTTON_DPAD, PlayalongInput.BUTTON_A_OR_DPAD), value.ordinal shl (povCode + 2), false)
            any = true
        }
        if (buttonRight is ControllerInput.Pov && buttonRight.povCode == povCode && (buttonRight.direction == value || release)) {
            playalong.handleInput(!release, EnumSet.of(PlayalongInput.BUTTON_DPAD_RIGHT, PlayalongInput.BUTTON_DPAD, PlayalongInput.BUTTON_A_OR_DPAD), value.ordinal shl (povCode + 2), false)
            any = true
        }
        if (buttonUp is ControllerInput.Pov && buttonUp.povCode == povCode && (buttonUp.direction == value || release)) {
            playalong.handleInput(!release, EnumSet.of(PlayalongInput.BUTTON_DPAD_UP, PlayalongInput.BUTTON_DPAD, PlayalongInput.BUTTON_A_OR_DPAD), value.ordinal shl (povCode + 2), false)
            any = true
        }
        if (buttonDown is ControllerInput.Pov && buttonDown.povCode == povCode && (buttonDown.direction == value || release)) {
            playalong.handleInput(!release, EnumSet.of(PlayalongInput.BUTTON_DPAD_DOWN, PlayalongInput.BUTTON_DPAD, PlayalongInput.BUTTON_A_OR_DPAD), value.ordinal shl (povCode + 2), false)
            any = true
        }
        return any
    }

    // Below not implemented
    override fun axisMoved(controller: Controller, axisCode: Int, value: Float): Boolean = false

    override fun accelerometerMoved(controller: Controller, accelerometerCode: Int, value: Vector3): Boolean = false

    override fun xSliderMoved(controller: Controller, sliderCode: Int, value: Boolean): Boolean = false

    override fun ySliderMoved(controller: Controller, sliderCode: Int, value: Boolean): Boolean = false


}