/* 
 *  AppStore remote controler RX
 *  board:      Arduino Pro Micro (Atmega32U4) 5V
 *  RX module:  RF433
 */
#include <VirtualWire.h> /* Mike McCauley */
#include <Keyboard.h>

#define BT_PRESSED 0
#define BT_RELEASE_ALL 255

typedef struct Buttons_bits_tag{
    bool A      : 1;
    bool B      : 1;
    bool start  : 1;
    bool select : 1;
    bool right  : 1;
    bool left   : 1;
    bool up     : 1;
    bool down   : 1;
}Buttons_bits_t;

typedef union Buttons_States_tag{
    uint8_t sbyte;
    Buttons_bits_t bits;
}Buttons_States_t;

Buttons_States_t buttons;

void setup() {
    /* RX module init */    
    vw_set_ptt_inverted(true);
    vw_setup(3000);
    vw_set_rx_pin(A0);
    vw_rx_start();

    /* release all buttons */
    buttons.sbyte = BT_RELEASE_ALL;
     
    Keyboard.begin();
}
void loop() {
    uint8_t msg[1];
    uint8_t size = 1;
    
    if (vw_get_message(msg, &size))
    {
        buttons.sbyte = (uint8_t)msg[0];
    }
    if(buttons.bits.A == BT_PRESSED)      Keyboard.press(',');
    else                                  Keyboard.release(',');
    if(buttons.bits.B == BT_PRESSED)      Keyboard.press('.');
    else                                  Keyboard.release('.');
    if(buttons.bits.down == BT_PRESSED)   Keyboard.press(KEY_DOWN_ARROW);
    else                                  Keyboard.release(KEY_DOWN_ARROW);
    if(buttons.bits.up == BT_PRESSED)     Keyboard.press(KEY_UP_ARROW);
    else                                  Keyboard.release(KEY_UP_ARROW);
    if(buttons.bits.left == BT_PRESSED)   Keyboard.press(KEY_LEFT_ARROW);
    else                                  Keyboard.release(KEY_LEFT_ARROW);
    if(buttons.bits.right == BT_PRESSED)  Keyboard.press(KEY_RIGHT_ARROW);
    else                                  Keyboard.release(KEY_RIGHT_ARROW);
    if(buttons.bits.start == BT_PRESSED)  Keyboard.press(KEY_RETURN);
    else                                  Keyboard.release(KEY_RETURN);
    if(buttons.bits.select == BT_PRESSED) Keyboard.press(KEY_RIGHT_SHIFT);
    else                                  Keyboard.release(KEY_RIGHT_SHIFT);
}
