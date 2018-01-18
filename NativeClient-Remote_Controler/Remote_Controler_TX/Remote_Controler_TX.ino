/* 
 *  AppStore remote controler TX
 *  board:      Arduino uno 328P 5V
 *  TX module:  RF433
 */
#include <VirtualWire.h> /* Mike McCauley */

/* buttons pins */
typedef enum Button_Pin_tag{
    BT_B =      3,
    BT_D =      4,
    BT_A =      5,
    BT_C =      6,
    BT_START =  7,
    BT_SELECT = 8,
    BT_RIGHT =  9,
    BT_DOWN =   11,
    BT_UP =     12,
    BT_LEFT =   13,
}Button_Pin_t;

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
    /* buttons pins set as inputs */
    pinMode(BT_A,      INPUT);
    pinMode(BT_B,      INPUT);  
    pinMode(BT_C,      INPUT);  
    pinMode(BT_D,      INPUT);  
    pinMode(BT_START,  INPUT);  
    pinMode(BT_SELECT, INPUT);  
    pinMode(BT_RIGHT,  INPUT);    
    pinMode(BT_DOWN,   INPUT);  
    pinMode(BT_UP,     INPUT);
    pinMode(BT_LEFT,   INPUT);

    /* buttons internal pull-up resistors */
    digitalWrite(BT_A,      HIGH);
    digitalWrite(BT_B,      HIGH);
    digitalWrite(BT_C,      HIGH);
    digitalWrite(BT_D,      HIGH);
    digitalWrite(BT_START,  HIGH);
    digitalWrite(BT_SELECT, HIGH);
    digitalWrite(BT_RIGHT,  HIGH);
    digitalWrite(BT_DOWN,   HIGH);
    digitalWrite(BT_UP,     HIGH);
    digitalWrite(BT_LEFT,   HIGH);
    
    /* TX module init */
    vw_set_ptt_inverted(true);
    vw_setup(3000);
    vw_set_tx_pin(10);
}

void loop() {
    /* mapping C button to A*/
    if (digitalRead(BT_A) == LOW || digitalRead(BT_C) == LOW)
    {
        buttons.bits.A = LOW;
    }
    else
    {
        buttons.bits.A = HIGH;
    }
    
    /* mapping D button to B*/
    if (digitalRead(BT_B) == LOW || digitalRead(BT_D) == LOW)
    {
        buttons.bits.B = LOW;
    }
    else
    {
        buttons.bits.B = HIGH;
    }
    buttons.bits.start =  digitalRead(BT_START);
    buttons.bits.select = digitalRead(BT_SELECT);
    buttons.bits.right =  digitalRead(BT_RIGHT);
    buttons.bits.left =   digitalRead(BT_LEFT);
    buttons.bits.down =   digitalRead(BT_DOWN);
    buttons.bits.up =     digitalRead(BT_UP); 
   
    vw_send(&buttons.sbyte, 1);
    vw_wait_tx();
}
