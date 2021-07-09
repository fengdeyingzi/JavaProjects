package android.view;

import java.util.ArrayList;

public class MotionEvent {
	
	   public static final int INVALID_POINTER_ID = -1;

	    /**
	     * Bit mask of the parts of the action code that are the action itself.
	     */
	    public static final int ACTION_MASK             = 0xff;

	    /**
	     * Constant for {@link #getActionMasked}: A pressed gesture has started, the
	     * motion contains the initial starting location.
	     * <p>
	     * This is also a good time to check the button state to distinguish
	     * secondary and tertiary button clicks and handle them appropriately.
	     * Use {@link #getButtonState} to retrieve the button state.
	     * </p>
	     */
	    public static final int ACTION_DOWN             = 0;

	    /**
	     * Constant for {@link #getActionMasked}: A pressed gesture has finished, the
	     * motion contains the final release location as well as any intermediate
	     * points since the last down or move event.
	     */
	    public static final int ACTION_UP               = 1;

	    /**
	     * Constant for {@link #getActionMasked}: A change has happened during a
	     * press gesture (between {@link #ACTION_DOWN} and {@link #ACTION_UP}).
	     * The motion contains the most recent point, as well as any intermediate
	     * points since the last down or move event.
	     */
	    public static final int ACTION_MOVE             = 2;

	    /**
	     * Constant for {@link #getActionMasked}: The current gesture has been aborted.
	     * You will not receive any more points in it.  You should treat this as
	     * an up event, but not perform any action that you normally would.
	     */
	    public static final int ACTION_CANCEL           = 3;

	    /**
	     * Constant for {@link #getActionMasked}: A movement has happened outside of the
	     * normal bounds of the UI element.  This does not provide a full gesture,
	     * but only the initial location of the movement/touch.
	     * <p>
	     * Note: Because the location of any event will be outside the
	     * bounds of the view hierarchy, it will not get dispatched to
	     * any children of a ViewGroup by default. Therefore,
	     * movements with ACTION_OUTSIDE should be handled in either the
	     * root {@link View} or in the appropriate {@link Window.Callback}
	     * (e.g. {@link android.app.Activity} or {@link android.app.Dialog}).
	     * </p>
	     */
	    public static final int ACTION_OUTSIDE          = 4;

	    /**
	     * Constant for {@link #getActionMasked}: A non-primary pointer has gone down.
	     * <p>
	     * Use {@link #getActionIndex} to retrieve the index of the pointer that changed.
	     * </p><p>
	     * The index is encoded in the {@link #ACTION_POINTER_INDEX_MASK} bits of the
	     * unmasked action returned by {@link #getAction}.
	     * </p>
	     */
	    public static final int ACTION_POINTER_DOWN     = 5;

	    /**
	     * Constant for {@link #getActionMasked}: A non-primary pointer has gone up.
	     * <p>
	     * Use {@link #getActionIndex} to retrieve the index of the pointer that changed.
	     * </p><p>
	     * The index is encoded in the {@link #ACTION_POINTER_INDEX_MASK} bits of the
	     * unmasked action returned by {@link #getAction}.
	     * </p>
	     */
	    public static final int ACTION_POINTER_UP       = 6;

	    /**
	     * Constant for {@link #getActionMasked}: A change happened but the pointer
	     * is not down (unlike {@link #ACTION_MOVE}).  The motion contains the most
	     * recent point, as well as any intermediate points since the last
	     * hover move event.
	     * <p>
	     * This action is always delivered to the window or view under the pointer.
	     * </p><p>
	     * This action is not a touch event so it is delivered to
	     * {@link View#onGenericMotionEvent(MotionEvent)} rather than
	     * {@link View#onTouchEvent(MotionEvent)}.
	     * </p>
	     */
	    public static final int ACTION_HOVER_MOVE       = 7;

	    /**
	     * Constant for {@link #getActionMasked}: The motion event contains relative
	     * vertical and/or horizontal scroll offsets.  Use {@link #getAxisValue(int)}
	     * to retrieve the information from {@link #AXIS_VSCROLL} and {@link #AXIS_HSCROLL}.
	     * The pointer may or may not be down when this event is dispatched.
	     * <p>
	     * This action is always delivered to the window or view under the pointer, which
	     * may not be the window or view currently touched.
	     * </p><p>
	     * This action is not a touch event so it is delivered to
	     * {@link View#onGenericMotionEvent(MotionEvent)} rather than
	     * {@link View#onTouchEvent(MotionEvent)}.
	     * </p>
	     */
	    public static final int ACTION_SCROLL           = 8;

	    /**
	     * Constant for {@link #getActionMasked}: The pointer is not down but has entered the
	     * boundaries of a window or view.
	     * <p>
	     * This action is always delivered to the window or view under the pointer.
	     * </p><p>
	     * This action is not a touch event so it is delivered to
	     * {@link View#onGenericMotionEvent(MotionEvent)} rather than
	     * {@link View#onTouchEvent(MotionEvent)}.
	     * </p>
	     */
	    public static final int ACTION_HOVER_ENTER      = 9;

	    /**
	     * Constant for {@link #getActionMasked}: The pointer is not down but has exited the
	     * boundaries of a window or view.
	     * <p>
	     * This action is always delivered to the window or view that was previously under the pointer.
	     * </p><p>
	     * This action is not a touch event so it is delivered to
	     * {@link View#onGenericMotionEvent(MotionEvent)} rather than
	     * {@link View#onTouchEvent(MotionEvent)}.
	     * </p>
	     */
	    public static final int ACTION_HOVER_EXIT       = 10;

	    /**
	     * Constant for {@link #getActionMasked}: A button has been pressed.
	     *
	     * <p>
	     * Use {@link #getActionButton()} to get which button was pressed.
	     * </p><p>
	     * This action is not a touch event so it is delivered to
	     * {@link View#onGenericMotionEvent(MotionEvent)} rather than
	     * {@link View#onTouchEvent(MotionEvent)}.
	     * </p>
	     */
	    public static final int ACTION_BUTTON_PRESS   = 11;

	    /**
	     * Constant for {@link #getActionMasked}: A button has been released.
	     *
	     * <p>
	     * Use {@link #getActionButton()} to get which button was released.
	     * </p><p>
	     * This action is not a touch event so it is delivered to
	     * {@link View#onGenericMotionEvent(MotionEvent)} rather than
	     * {@link View#onTouchEvent(MotionEvent)}.
	     * </p>
	     */
	    public static final int ACTION_BUTTON_RELEASE  = 12;

	    /**
	     * Bits in the action code that represent a pointer index, used with
	     * {@link #ACTION_POINTER_DOWN} and {@link #ACTION_POINTER_UP}.  Shifting
	     * down by {@link #ACTION_POINTER_INDEX_SHIFT} provides the actual pointer
	     * index where the data for the pointer going up or down can be found; you can
	     * get its identifier with {@link #getPointerId(int)} and the actual
	     * data with {@link #getX(int)} etc.
	     *
	     * @see #getActionIndex
	     */
	    public static final int ACTION_POINTER_INDEX_MASK  = 0xff00;

	    /**
	     * Bit shift for the action bits holding the pointer index as
	     * defined by {@link #ACTION_POINTER_INDEX_MASK}.
	     *
	     * @see #getActionIndex
	     */
	    public static final int ACTION_POINTER_INDEX_SHIFT = 8;

	    /**
	     * @deprecated Use {@link #ACTION_POINTER_INDEX_MASK} to retrieve the
	     * data index associated with {@link #ACTION_POINTER_DOWN}.
	     */
	    @Deprecated
	    public static final int ACTION_POINTER_1_DOWN   = ACTION_POINTER_DOWN | 0x0000;

	    /**
	     * @deprecated Use {@link #ACTION_POINTER_INDEX_MASK} to retrieve the
	     * data index associated with {@link #ACTION_POINTER_DOWN}.
	     */
	    @Deprecated
	    public static final int ACTION_POINTER_2_DOWN   = ACTION_POINTER_DOWN | 0x0100;

	    /**
	     * @deprecated Use {@link #ACTION_POINTER_INDEX_MASK} to retrieve the
	     * data index associated with {@link #ACTION_POINTER_DOWN}.
	     */
	    @Deprecated
	    public static final int ACTION_POINTER_3_DOWN   = ACTION_POINTER_DOWN | 0x0200;

	    /**
	     * @deprecated Use {@link #ACTION_POINTER_INDEX_MASK} to retrieve the
	     * data index associated with {@link #ACTION_POINTER_UP}.
	     */
	    @Deprecated
	    public static final int ACTION_POINTER_1_UP     = ACTION_POINTER_UP | 0x0000;

	    /**
	     * @deprecated Use {@link #ACTION_POINTER_INDEX_MASK} to retrieve the
	     * data index associated with {@link #ACTION_POINTER_UP}.
	     */
	    @Deprecated
	    public static final int ACTION_POINTER_2_UP     = ACTION_POINTER_UP | 0x0100;

	    /**
	     * @deprecated Use {@link #ACTION_POINTER_INDEX_MASK} to retrieve the
	     * data index associated with {@link #ACTION_POINTER_UP}.
	     */
	    @Deprecated
	    public static final int ACTION_POINTER_3_UP     = ACTION_POINTER_UP | 0x0200;

	    /**
	     * @deprecated Renamed to {@link #ACTION_POINTER_INDEX_MASK} to match
	     * the actual data contained in these bits.
	     */
	    @Deprecated
	    public static final int ACTION_POINTER_ID_MASK  = 0xff00;

	    /**
	     * @deprecated Renamed to {@link #ACTION_POINTER_INDEX_SHIFT} to match
	     * the actual data contained in these bits.
	     */
	    @Deprecated
	    public static final int ACTION_POINTER_ID_SHIFT = 8;

	    /**
	     * This flag indicates that the window that received this motion event is partly
	     * or wholly obscured by another visible window above it.  This flag is set to true
	     * even if the event did not directly pass through the obscured area.
	     * A security sensitive application can check this flag to identify situations in which
	     * a malicious application may have covered up part of its content for the purpose
	     * of misleading the user or hijacking touches.  An appropriate response might be
	     * to drop the suspect touches or to take additional precautions to confirm the user's
	     * actual intent.
	     */
	    public static final int FLAG_WINDOW_IS_OBSCURED = 0x1;

	    /**
	     * This flag indicates that the window that received this motion event is partly
	     * or wholly obscured by another visible window above it.  This flag is set to true
	     * even if the event did not directly pass through the obscured area.
	     * A security sensitive application can check this flag to identify situations in which
	     * a malicious application may have covered up part of its content for the purpose
	     * of misleading the user or hijacking touches.  An appropriate response might be
	     * to drop the suspect touches or to take additional precautions to confirm the user's
	     * actual intent.
	     *
	     * Unlike FLAG_WINDOW_IS_OBSCURED, this is actually true.
	     * @hide
	     */
	    public static final int FLAG_WINDOW_IS_PARTIALLY_OBSCURED = 0x2;

	    /**
	     * This private flag is only set on {@link #ACTION_HOVER_MOVE} events and indicates that
	     * this event will be immediately followed by a {@link #ACTION_HOVER_EXIT}. It is used to
	     * prevent generating redundant {@link #ACTION_HOVER_ENTER} events.
	     * @hide
	     */
	    public static final int FLAG_HOVER_EXIT_PENDING = 0x4;

	    /**
	     * This flag indicates that the event has been generated by a gesture generator. It
	     * provides a hint to the GestureDector to not apply any touch slop.
	     *
	     * @hide
	     */
	    public static final int FLAG_IS_GENERATED_GESTURE = 0x8;

	    /**
	     * Private flag that indicates when the system has detected that this motion event
	     * may be inconsistent with respect to the sequence of previously delivered motion events,
	     * such as when a pointer move event is sent but the pointer is not down.
	     *
	     * @hide
	     * @see #isTainted
	     * @see #setTainted
	     */
	    public static final int FLAG_TAINTED = 0x80000000;

	    /**
	     * Private flag indicating that this event was synthesized by the system and
	     * should be delivered to the accessibility focused view first. When being
	     * dispatched such an event is not handled by predecessors of the accessibility
	     * focused view and after the event reaches that view the flag is cleared and
	     * normal event dispatch is performed. This ensures that the platform can click
	     * on any view that has accessibility focus which is semantically equivalent to
	     * asking the view to perform a click accessibility action but more generic as
	     * views not implementing click action correctly can still be activated.
	     *
	     * @hide
	     * @see #isTargetAccessibilityFocus()
	     * @see #setTargetAccessibilityFocus(boolean)
	     */
	    public static final int FLAG_TARGET_ACCESSIBILITY_FOCUS = 0x40000000;


	    /**
	     * Flag indicating the motion event intersected the top edge of the screen.
	     */
	    public static final int EDGE_TOP = 0x00000001;

	    /**
	     * Flag indicating the motion event intersected the bottom edge of the screen.
	     */
	    public static final int EDGE_BOTTOM = 0x00000002;

	    /**
	     * Flag indicating the motion event intersected the left edge of the screen.
	     */
	    public static final int EDGE_LEFT = 0x00000004;

	    /**
	     * Flag indicating the motion event intersected the right edge of the screen.
	     */
	    public static final int EDGE_RIGHT = 0x00000008;

	    /**
	     * Axis constant: X axis of a motion event.
	     * <p>
	     * <ul>
	     * <li>For a touch screen, reports the absolute X screen position of the center of
	     * the touch contact area.  The units are display pixels.
	     * <li>For a touch pad, reports the absolute X surface position of the center of the touch
	     * contact area.  The units are device-dependent; use {@link InputDevice#getMotionRange(int)}
	     * to query the effective range of values.
	     * <li>For a mouse, reports the absolute X screen position of the mouse pointer.
	     * The units are display pixels.
	     * <li>For a trackball, reports the relative horizontal displacement of the trackball.
	     * The value is normalized to a range from -1.0 (left) to 1.0 (right).
	     * <li>For a joystick, reports the absolute X position of the joystick.
	     * The value is normalized to a range from -1.0 (left) to 1.0 (right).
	     * </ul>
	     * </p>
	     *
	     * @see #getX(int)
	     * @see #getHistoricalX(int, int)
	     * @see MotionEvent.PointerCoords#x
	     * @see InputDevice#getMotionRange
	     */
	    public static final int AXIS_X = 0;

	    /**
	     * Axis constant: Y axis of a motion event.
	     * <p>
	     * <ul>
	     * <li>For a touch screen, reports the absolute Y screen position of the center of
	     * the touch contact area.  The units are display pixels.
	     * <li>For a touch pad, reports the absolute Y surface position of the center of the touch
	     * contact area.  The units are device-dependent; use {@link InputDevice#getMotionRange(int)}
	     * to query the effective range of values.
	     * <li>For a mouse, reports the absolute Y screen position of the mouse pointer.
	     * The units are display pixels.
	     * <li>For a trackball, reports the relative vertical displacement of the trackball.
	     * The value is normalized to a range from -1.0 (up) to 1.0 (down).
	     * <li>For a joystick, reports the absolute Y position of the joystick.
	     * The value is normalized to a range from -1.0 (up or far) to 1.0 (down or near).
	     * </ul>
	     * </p>
	     *
	     * @see #getY(int)
	     * @see #getHistoricalY(int, int)
	     * @see MotionEvent.PointerCoords#y
	     * @see InputDevice#getMotionRange
	     */
	    public static final int AXIS_Y = 1;

	    /**
	     * Axis constant: Pressure axis of a motion event.
	     * <p>
	     * <ul>
	     * <li>For a touch screen or touch pad, reports the approximate pressure applied to the surface
	     * by a finger or other tool.  The value is normalized to a range from
	     * 0 (no pressure at all) to 1 (normal pressure), although values higher than 1
	     * may be generated depending on the calibration of the input device.
	     * <li>For a trackball, the value is set to 1 if the trackball button is pressed
	     * or 0 otherwise.
	     * <li>For a mouse, the value is set to 1 if the primary mouse button is pressed
	     * or 0 otherwise.
	     * </ul>
	     * </p>
	     *
	     * @see #getPressure(int)
	     * @see #getHistoricalPressure(int, int)
	     * @see MotionEvent.PointerCoords#pressure
	     * @see InputDevice#getMotionRange
	     */
	    public static final int AXIS_PRESSURE = 2;

	    /**
	     * Axis constant: Size axis of a motion event.
	     * <p>
	     * <ul>
	     * <li>For a touch screen or touch pad, reports the approximate size of the contact area in
	     * relation to the maximum detectable size for the device.  The value is normalized
	     * to a range from 0 (smallest detectable size) to 1 (largest detectable size),
	     * although it is not a linear scale.  This value is of limited use.
	     * To obtain calibrated size information, use
	     * {@link #AXIS_TOUCH_MAJOR} or {@link #AXIS_TOOL_MAJOR}.
	     * </ul>
	     * </p>
	     *
	     * @see #getSize(int)
	     * @see #getHistoricalSize(int, int)
	     * @see MotionEvent.PointerCoords#size
	     * @see InputDevice#getMotionRange
	     */
	    public static final int AXIS_SIZE = 3;

	    /**
	     * Axis constant: TouchMajor axis of a motion event.
	     * <p>
	     * <ul>
	     * <li>For a touch screen, reports the length of the major axis of an ellipse that
	     * represents the touch area at the point of contact.
	     * The units are display pixels.
	     * <li>For a touch pad, reports the length of the major axis of an ellipse that
	     * represents the touch area at the point of contact.
	     * The units are device-dependent; use {@link InputDevice#getMotionRange(int)}
	     * to query the effective range of values.
	     * </ul>
	     * </p>
	     *
	     * @see #getTouchMajor(int)
	     * @see #getHistoricalTouchMajor(int, int)
	     * @see MotionEvent.PointerCoords#touchMajor
	     * @see InputDevice#getMotionRange
	     */
	    public static final int AXIS_TOUCH_MAJOR = 4;

	    /**
	     * Axis constant: TouchMinor axis of a motion event.
	     * <p>
	     * <ul>
	     * <li>For a touch screen, reports the length of the minor axis of an ellipse that
	     * represents the touch area at the point of contact.
	     * The units are display pixels.
	     * <li>For a touch pad, reports the length of the minor axis of an ellipse that
	     * represents the touch area at the point of contact.
	     * The units are device-dependent; use {@link InputDevice#getMotionRange(int)}
	     * to query the effective range of values.
	     * </ul>
	     * </p><p>
	     * When the touch is circular, the major and minor axis lengths will be equal to one another.
	     * </p>
	     *
	     * @see #getTouchMinor(int)
	     * @see #getHistoricalTouchMinor(int, int)
	     * @see MotionEvent.PointerCoords#touchMinor
	     * @see InputDevice#getMotionRange
	     */
	    public static final int AXIS_TOUCH_MINOR = 5;

	    /**
	     * Axis constant: ToolMajor axis of a motion event.
	     * <p>
	     * <ul>
	     * <li>For a touch screen, reports the length of the major axis of an ellipse that
	     * represents the size of the approaching finger or tool used to make contact.
	     * <li>For a touch pad, reports the length of the major axis of an ellipse that
	     * represents the size of the approaching finger or tool used to make contact.
	     * The units are device-dependent; use {@link InputDevice#getMotionRange(int)}
	     * to query the effective range of values.
	     * </ul>
	     * </p><p>
	     * When the touch is circular, the major and minor axis lengths will be equal to one another.
	     * </p><p>
	     * The tool size may be larger than the touch size since the tool may not be fully
	     * in contact with the touch sensor.
	     * </p>
	     *
	     * @see #getToolMajor(int)
	     * @see #getHistoricalToolMajor(int, int)
	     * @see MotionEvent.PointerCoords#toolMajor
	     * @see InputDevice#getMotionRange
	     */
	    public static final int AXIS_TOOL_MAJOR = 6;

	    /**
	     * Axis constant: ToolMinor axis of a motion event.
	     * <p>
	     * <ul>
	     * <li>For a touch screen, reports the length of the minor axis of an ellipse that
	     * represents the size of the approaching finger or tool used to make contact.
	     * <li>For a touch pad, reports the length of the minor axis of an ellipse that
	     * represents the size of the approaching finger or tool used to make contact.
	     * The units are device-dependent; use {@link InputDevice#getMotionRange(int)}
	     * to query the effective range of values.
	     * </ul>
	     * </p><p>
	     * When the touch is circular, the major and minor axis lengths will be equal to one another.
	     * </p><p>
	     * The tool size may be larger than the touch size since the tool may not be fully
	     * in contact with the touch sensor.
	     * </p>
	     *
	     * @see #getToolMinor(int)
	     * @see #getHistoricalToolMinor(int, int)
	     * @see MotionEvent.PointerCoords#toolMinor
	     * @see InputDevice#getMotionRange
	     */
	    public static final int AXIS_TOOL_MINOR = 7;

	    /**
	     * Axis constant: Orientation axis of a motion event.
	     * <p>
	     * <ul>
	     * <li>For a touch screen or touch pad, reports the orientation of the finger
	     * or tool in radians relative to the vertical plane of the device.
	     * An angle of 0 radians indicates that the major axis of contact is oriented
	     * upwards, is perfectly circular or is of unknown orientation.  A positive angle
	     * indicates that the major axis of contact is oriented to the right.  A negative angle
	     * indicates that the major axis of contact is oriented to the left.
	     * The full range is from -PI/2 radians (finger pointing fully left) to PI/2 radians
	     * (finger pointing fully right).
	     * <li>For a stylus, the orientation indicates the direction in which the stylus
	     * is pointing in relation to the vertical axis of the current orientation of the screen.
	     * The range is from -PI radians to PI radians, where 0 is pointing up,
	     * -PI/2 radians is pointing left, -PI or PI radians is pointing down, and PI/2 radians
	     * is pointing right.  See also {@link #AXIS_TILT}.
	     * </ul>
	     * </p>
	     *
	     * @see #getOrientation(int)
	     * @see #getHistoricalOrientation(int, int)
	     * @see MotionEvent.PointerCoords#orientation
	     * @see InputDevice#getMotionRange
	     */
	    public static final int AXIS_ORIENTATION = 8;

	    /**
	     * Axis constant: Vertical Scroll axis of a motion event.
	     * <p>
	     * <ul>
	     * <li>For a mouse, reports the relative movement of the vertical scroll wheel.
	     * The value is normalized to a range from -1.0 (down) to 1.0 (up).
	     * </ul>
	     * </p><p>
	     * This axis should be used to scroll views vertically.
	     * </p>
	     *
	     * @see #getAxisValue(int, int)
	     * @see #getHistoricalAxisValue(int, int, int)
	     * @see MotionEvent.PointerCoords#getAxisValue(int)
	     * @see InputDevice#getMotionRange
	     */
	    public static final int AXIS_VSCROLL = 9;

	    /**
	     * Axis constant: Horizontal Scroll axis of a motion event.
	     * <p>
	     * <ul>
	     * <li>For a mouse, reports the relative movement of the horizontal scroll wheel.
	     * The value is normalized to a range from -1.0 (left) to 1.0 (right).
	     * </ul>
	     * </p><p>
	     * This axis should be used to scroll views horizontally.
	     * </p>
	     *
	     * @see #getAxisValue(int, int)
	     * @see #getHistoricalAxisValue(int, int, int)
	     * @see MotionEvent.PointerCoords#getAxisValue(int)
	     * @see InputDevice#getMotionRange
	     */
	    public static final int AXIS_HSCROLL = 10;

	    /**
	     * Axis constant: Z axis of a motion event.
	     * <p>
	     * <ul>
	     * <li>For a joystick, reports the absolute Z position of the joystick.
	     * The value is normalized to a range from -1.0 (high) to 1.0 (low).
	     * <em>On game pads with two analog joysticks, this axis is often reinterpreted
	     * to report the absolute X position of the second joystick instead.</em>
	     * </ul>
	     * </p>
	     *
	     * @see #getAxisValue(int, int)
	     * @see #getHistoricalAxisValue(int, int, int)
	     * @see MotionEvent.PointerCoords#getAxisValue(int)
	     * @see InputDevice#getMotionRange
	     */
	    public static final int AXIS_Z = 11;

	    /**
	     * Axis constant: X Rotation axis of a motion event.
	     * <p>
	     * <ul>
	     * <li>For a joystick, reports the absolute rotation angle about the X axis.
	     * The value is normalized to a range from -1.0 (counter-clockwise) to 1.0 (clockwise).
	     * </ul>
	     * </p>
	     *
	     * @see #getAxisValue(int, int)
	     * @see #getHistoricalAxisValue(int, int, int)
	     * @see MotionEvent.PointerCoords#getAxisValue(int)
	     * @see InputDevice#getMotionRange
	     */
	    public static final int AXIS_RX = 12;

	    /**
	     * Axis constant: Y Rotation axis of a motion event.
	     * <p>
	     * <ul>
	     * <li>For a joystick, reports the absolute rotation angle about the Y axis.
	     * The value is normalized to a range from -1.0 (counter-clockwise) to 1.0 (clockwise).
	     * </ul>
	     * </p>
	     *
	     * @see #getAxisValue(int, int)
	     * @see #getHistoricalAxisValue(int, int, int)
	     * @see MotionEvent.PointerCoords#getAxisValue(int)
	     * @see InputDevice#getMotionRange
	     */
	    public static final int AXIS_RY = 13;

	    /**
	     * Axis constant: Z Rotation axis of a motion event.
	     * <p>
	     * <ul>
	     * <li>For a joystick, reports the absolute rotation angle about the Z axis.
	     * The value is normalized to a range from -1.0 (counter-clockwise) to 1.0 (clockwise).
	     * <em>On game pads with two analog joysticks, this axis is often reinterpreted
	     * to report the absolute Y position of the second joystick instead.</em>
	     * </ul>
	     * </p>
	     *
	     * @see #getAxisValue(int, int)
	     * @see #getHistoricalAxisValue(int, int, int)
	     * @see MotionEvent.PointerCoords#getAxisValue(int)
	     * @see InputDevice#getMotionRange
	     */
	    public static final int AXIS_RZ = 14;

	    /**
	     * Axis constant: Hat X axis of a motion event.
	     * <p>
	     * <ul>
	     * <li>For a joystick, reports the absolute X position of the directional hat control.
	     * The value is normalized to a range from -1.0 (left) to 1.0 (right).
	     * </ul>
	     * </p>
	     *
	     * @see #getAxisValue(int, int)
	     * @see #getHistoricalAxisValue(int, int, int)
	     * @see MotionEvent.PointerCoords#getAxisValue(int)
	     * @see InputDevice#getMotionRange
	     */
	    public static final int AXIS_HAT_X = 15;

	    /**
	     * Axis constant: Hat Y axis of a motion event.
	     * <p>
	     * <ul>
	     * <li>For a joystick, reports the absolute Y position of the directional hat control.
	     * The value is normalized to a range from -1.0 (up) to 1.0 (down).
	     * </ul>
	     * </p>
	     *
	     * @see #getAxisValue(int, int)
	     * @see #getHistoricalAxisValue(int, int, int)
	     * @see MotionEvent.PointerCoords#getAxisValue(int)
	     * @see InputDevice#getMotionRange
	     */
	    public static final int AXIS_HAT_Y = 16;

	    /**
	     * Axis constant: Left Trigger axis of a motion event.
	     * <p>
	     * <ul>
	     * <li>For a joystick, reports the absolute position of the left trigger control.
	     * The value is normalized to a range from 0.0 (released) to 1.0 (fully pressed).
	     * </ul>
	     * </p>
	     *
	     * @see #getAxisValue(int, int)
	     * @see #getHistoricalAxisValue(int, int, int)
	     * @see MotionEvent.PointerCoords#getAxisValue(int)
	     * @see InputDevice#getMotionRange
	     */
	    public static final int AXIS_LTRIGGER = 17;

	    /**
	     * Axis constant: Right Trigger axis of a motion event.
	     * <p>
	     * <ul>
	     * <li>For a joystick, reports the absolute position of the right trigger control.
	     * The value is normalized to a range from 0.0 (released) to 1.0 (fully pressed).
	     * </ul>
	     * </p>
	     *
	     * @see #getAxisValue(int, int)
	     * @see #getHistoricalAxisValue(int, int, int)
	     * @see MotionEvent.PointerCoords#getAxisValue(int)
	     * @see InputDevice#getMotionRange
	     */
	    public static final int AXIS_RTRIGGER = 18;

	    /**
	     * Axis constant: Throttle axis of a motion event.
	     * <p>
	     * <ul>
	     * <li>For a joystick, reports the absolute position of the throttle control.
	     * The value is normalized to a range from 0.0 (fully open) to 1.0 (fully closed).
	     * </ul>
	     * </p>
	     *
	     * @see #getAxisValue(int, int)
	     * @see #getHistoricalAxisValue(int, int, int)
	     * @see MotionEvent.PointerCoords#getAxisValue(int)
	     * @see InputDevice#getMotionRange
	     */
	    public static final int AXIS_THROTTLE = 19;

	    /**
	     * Axis constant: Rudder axis of a motion event.
	     * <p>
	     * <ul>
	     * <li>For a joystick, reports the absolute position of the rudder control.
	     * The value is normalized to a range from -1.0 (turn left) to 1.0 (turn right).
	     * </ul>
	     * </p>
	     *
	     * @see #getAxisValue(int, int)
	     * @see #getHistoricalAxisValue(int, int, int)
	     * @see MotionEvent.PointerCoords#getAxisValue(int)
	     * @see InputDevice#getMotionRange
	     */
	    public static final int AXIS_RUDDER = 20;

	    /**
	     * Axis constant: Wheel axis of a motion event.
	     * <p>
	     * <ul>
	     * <li>For a joystick, reports the absolute position of the steering wheel control.
	     * The value is normalized to a range from -1.0 (turn left) to 1.0 (turn right).
	     * </ul>
	     * </p>
	     *
	     * @see #getAxisValue(int, int)
	     * @see #getHistoricalAxisValue(int, int, int)
	     * @see MotionEvent.PointerCoords#getAxisValue(int)
	     * @see InputDevice#getMotionRange
	     */
	    public static final int AXIS_WHEEL = 21;

	    /**
	     * Axis constant: Gas axis of a motion event.
	     * <p>
	     * <ul>
	     * <li>For a joystick, reports the absolute position of the gas (accelerator) control.
	     * The value is normalized to a range from 0.0 (no acceleration)
	     * to 1.0 (maximum acceleration).
	     * </ul>
	     * </p>
	     *
	     * @see #getAxisValue(int, int)
	     * @see #getHistoricalAxisValue(int, int, int)
	     * @see MotionEvent.PointerCoords#getAxisValue(int)
	     * @see InputDevice#getMotionRange
	     */
	    public static final int AXIS_GAS = 22;

	    /**
	     * Axis constant: Brake axis of a motion event.
	     * <p>
	     * <ul>
	     * <li>For a joystick, reports the absolute position of the brake control.
	     * The value is normalized to a range from 0.0 (no braking) to 1.0 (maximum braking).
	     * </ul>
	     * </p>
	     *
	     * @see #getAxisValue(int, int)
	     * @see #getHistoricalAxisValue(int, int, int)
	     * @see MotionEvent.PointerCoords#getAxisValue(int)
	     * @see InputDevice#getMotionRange
	     */
	    public static final int AXIS_BRAKE = 23;

	    /**
	     * Axis constant: Distance axis of a motion event.
	     * <p>
	     * <ul>
	     * <li>For a stylus, reports the distance of the stylus from the screen.
	     * A value of 0.0 indicates direct contact and larger values indicate increasing
	     * distance from the surface.
	     * </ul>
	     * </p>
	     *
	     * @see #getAxisValue(int, int)
	     * @see #getHistoricalAxisValue(int, int, int)
	     * @see MotionEvent.PointerCoords#getAxisValue(int)
	     * @see InputDevice#getMotionRange
	     */
	    public static final int AXIS_DISTANCE = 24;

	    /**
	     * Axis constant: Tilt axis of a motion event.
	     * <p>
	     * <ul>
	     * <li>For a stylus, reports the tilt angle of the stylus in radians where
	     * 0 radians indicates that the stylus is being held perpendicular to the
	     * surface, and PI/2 radians indicates that the stylus is being held flat
	     * against the surface.
	     * </ul>
	     * </p>
	     *
	     * @see #getAxisValue(int, int)
	     * @see #getHistoricalAxisValue(int, int, int)
	     * @see MotionEvent.PointerCoords#getAxisValue(int, int)
	     * @see InputDevice#getMotionRange
	     */
	    public static final int AXIS_TILT = 25;

	    /**
	     * Axis constant: Generic scroll axis of a motion event.
	     * <p>
	     * <ul>
	     * <li>Reports the relative movement of the generic scrolling device.
	     * </ul>
	     * </p><p>
	     * This axis should be used for scroll events that are neither strictly vertical nor horizontal.
	     * A good example would be the rotation of a rotary encoder input device.
	     * </p>
	     *
	     * @see #getAxisValue(int, int)
	     */
	    public static final int AXIS_SCROLL = 26;

	    /**
	     * Axis constant: The movement of x position of a motion event.
	     * <p>
	     * <ul>
	     * <li>For a mouse, reports a difference of x position between the previous position.
	     * This is useful when pointer is captured, in that case the mouse pointer doesn't change
	     * the location but this axis reports the difference which allows the app to see
	     * how the mouse is moved.
	     * </ul>
	     * </p>
	     *
	     * @see #getAxisValue(int, int)
	     * @see #getHistoricalAxisValue(int, int, int)
	     * @see MotionEvent.PointerCoords#getAxisValue(int, int)
	     * @see InputDevice#getMotionRange
	     */
	    public static final int AXIS_RELATIVE_X = 27;

	    /**
	     * Axis constant: The movement of y position of a motion event.
	     * <p>
	     * This is similar to {@link #AXIS_RELATIVE_X} but for y-axis.
	     * </p>
	     *
	     * @see #getAxisValue(int, int)
	     * @see #getHistoricalAxisValue(int, int, int)
	     * @see MotionEvent.PointerCoords#getAxisValue(int, int)
	     * @see InputDevice#getMotionRange
	     */
	    public static final int AXIS_RELATIVE_Y = 28;

	    /**
	     * Axis constant: Generic 1 axis of a motion event.
	     * The interpretation of a generic axis is device-specific.
	     *
	     * @see #getAxisValue(int, int)
	     * @see #getHistoricalAxisValue(int, int, int)
	     * @see MotionEvent.PointerCoords#getAxisValue(int)
	     * @see InputDevice#getMotionRange
	     */
	    public static final int AXIS_GENERIC_1 = 32;

	    /**
	     * Axis constant: Generic 2 axis of a motion event.
	     * The interpretation of a generic axis is device-specific.
	     *
	     * @see #getAxisValue(int, int)
	     * @see #getHistoricalAxisValue(int, int, int)
	     * @see MotionEvent.PointerCoords#getAxisValue(int)
	     * @see InputDevice#getMotionRange
	     */
	    public static final int AXIS_GENERIC_2 = 33;

	    /**
	     * Axis constant: Generic 3 axis of a motion event.
	     * The interpretation of a generic axis is device-specific.
	     *
	     * @see #getAxisValue(int, int)
	     * @see #getHistoricalAxisValue(int, int, int)
	     * @see MotionEvent.PointerCoords#getAxisValue(int)
	     * @see InputDevice#getMotionRange
	     */
	    public static final int AXIS_GENERIC_3 = 34;

	    /**
	     * Axis constant: Generic 4 axis of a motion event.
	     * The interpretation of a generic axis is device-specific.
	     *
	     * @see #getAxisValue(int, int)
	     * @see #getHistoricalAxisValue(int, int, int)
	     * @see MotionEvent.PointerCoords#getAxisValue(int)
	     * @see InputDevice#getMotionRange
	     */
	    public static final int AXIS_GENERIC_4 = 35;

	    /**
	     * Axis constant: Generic 5 axis of a motion event.
	     * The interpretation of a generic axis is device-specific.
	     *
	     * @see #getAxisValue(int, int)
	     * @see #getHistoricalAxisValue(int, int, int)
	     * @see MotionEvent.PointerCoords#getAxisValue(int)
	     * @see InputDevice#getMotionRange
	     */
	    public static final int AXIS_GENERIC_5 = 36;

	    /**
	     * Axis constant: Generic 6 axis of a motion event.
	     * The interpretation of a generic axis is device-specific.
	     *
	     * @see #getAxisValue(int, int)
	     * @see #getHistoricalAxisValue(int, int, int)
	     * @see MotionEvent.PointerCoords#getAxisValue(int)
	     * @see InputDevice#getMotionRange
	     */
	    public static final int AXIS_GENERIC_6 = 37;

	    /**
	     * Axis constant: Generic 7 axis of a motion event.
	     * The interpretation of a generic axis is device-specific.
	     *
	     * @see #getAxisValue(int, int)
	     * @see #getHistoricalAxisValue(int, int, int)
	     * @see MotionEvent.PointerCoords#getAxisValue(int)
	     * @see InputDevice#getMotionRange
	     */
	    public static final int AXIS_GENERIC_7 = 38;

	    /**
	     * Axis constant: Generic 8 axis of a motion event.
	     * The interpretation of a generic axis is device-specific.
	     *
	     * @see #getAxisValue(int, int)
	     * @see #getHistoricalAxisValue(int, int, int)
	     * @see MotionEvent.PointerCoords#getAxisValue(int)
	     * @see InputDevice#getMotionRange
	     */
	    public static final int AXIS_GENERIC_8 = 39;

	    /**
	     * Axis constant: Generic 9 axis of a motion event.
	     * The interpretation of a generic axis is device-specific.
	     *
	     * @see #getAxisValue(int, int)
	     * @see #getHistoricalAxisValue(int, int, int)
	     * @see MotionEvent.PointerCoords#getAxisValue(int)
	     * @see InputDevice#getMotionRange
	     */
	    public static final int AXIS_GENERIC_9 = 40;

	    /**
	     * Axis constant: Generic 10 axis of a motion event.
	     * The interpretation of a generic axis is device-specific.
	     *
	     * @see #getAxisValue(int, int)
	     * @see #getHistoricalAxisValue(int, int, int)
	     * @see MotionEvent.PointerCoords#getAxisValue(int)
	     * @see InputDevice#getMotionRange
	     */
	    public static final int AXIS_GENERIC_10 = 41;

	    /**
	     * Axis constant: Generic 11 axis of a motion event.
	     * The interpretation of a generic axis is device-specific.
	     *
	     * @see #getAxisValue(int, int)
	     * @see #getHistoricalAxisValue(int, int, int)
	     * @see MotionEvent.PointerCoords#getAxisValue(int)
	     * @see InputDevice#getMotionRange
	     */
	    public static final int AXIS_GENERIC_11 = 42;

	    /**
	     * Axis constant: Generic 12 axis of a motion event.
	     * The interpretation of a generic axis is device-specific.
	     *
	     * @see #getAxisValue(int, int)
	     * @see #getHistoricalAxisValue(int, int, int)
	     * @see MotionEvent.PointerCoords#getAxisValue(int)
	     * @see InputDevice#getMotionRange
	     */
	    public static final int AXIS_GENERIC_12 = 43;

	    /**
	     * Axis constant: Generic 13 axis of a motion event.
	     * The interpretation of a generic axis is device-specific.
	     *
	     * @see #getAxisValue(int, int)
	     * @see #getHistoricalAxisValue(int, int, int)
	     * @see MotionEvent.PointerCoords#getAxisValue(int)
	     * @see InputDevice#getMotionRange
	     */
	    public static final int AXIS_GENERIC_13 = 44;

	    /**
	     * Axis constant: Generic 14 axis of a motion event.
	     * The interpretation of a generic axis is device-specific.
	     *
	     * @see #getAxisValue(int, int)
	     * @see #getHistoricalAxisValue(int, int, int)
	     * @see MotionEvent.PointerCoords#getAxisValue(int)
	     * @see InputDevice#getMotionRange
	     */
	    public static final int AXIS_GENERIC_14 = 45;

	    /**
	     * Axis constant: Generic 15 axis of a motion event.
	     * The interpretation of a generic axis is device-specific.
	     *
	     * @see #getAxisValue(int, int)
	     * @see #getHistoricalAxisValue(int, int, int)
	     * @see MotionEvent.PointerCoords#getAxisValue(int)
	     * @see InputDevice#getMotionRange
	     */
	    public static final int AXIS_GENERIC_15 = 46;

	    /**
	     * Axis constant: Generic 16 axis of a motion event.
	     * The interpretation of a generic axis is device-specific.
	     *
	     * @see #getAxisValue(int, int)
	     * @see #getHistoricalAxisValue(int, int, int)
	     * @see MotionEvent.PointerCoords#getAxisValue(int)
	     * @see InputDevice#getMotionRange
	     */
	    public static final int AXIS_GENERIC_16 = 47;

	
	class Touch{
		float x;
		float y;
		int touch_id; //手指id
		public Touch(int x,int y){
			this.x = x;
			this.y = y;
		}
	}
	ArrayList<Touch> list_touch;
	private int mAction;
	
	public MotionEvent(){
		this.list_touch = new ArrayList<MotionEvent.Touch>();
	}

    public final float getX() {
        return list_touch.get(0).x;
    }

    /**
     * {@link #getY(int)} for the first pointer index (may be an
     * arbitrary pointer identifier).
     *
     * @see #AXIS_Y
     */
    public final float getY() {
        return list_touch.get(0).y;
    }
    
    public float getX(int index) {
		return list_touch.get(index).x;
	}
    
    public float getY(int index) {
		return list_touch.get(index).y;
	}
    
    public void addXY(int x,int y){
    	Touch touch = new Touch(x, y);
    	list_touch.add(touch);
    }
    
    public int getAction(){
    	return mAction;
    }
    
    public void setAction(int action){
    	this.mAction = action;
    }

}
