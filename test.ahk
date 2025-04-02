^!+t::
{
    SetKeyDelay 60                          ; Wait 60ms between each test keystroke.

                                            ; Lab 3 test script.
    SendEvent("5{enter}")                   ; Enter Customer Update submenu.
	SendEvent("5{enter}")                   ; Display CustomerData List. (Default.)
	SendEvent("1{enter}")                   ; Add a new Customer.
	SendEvent("George Washington{enter}")   ; New Customer name.
	SendEvent("834TRE-57{enter}")           ; New Customer acctNum. (Invalid, non-unique.)
	SendEvent("834TRE-58{enter}")           ; New Customer acctNum. (Valid.)
	SendEvent("2{enter}")                   ; Delete Customer.
	SendEvent("432TTT-22{enter}")           ; acctNum of Customer to delete. (Invalid, doesn't exist.)
	SendEvent("2{enter}")                   ; Delete Customer. (Need to pick again b/c reprompting forever is insane.)
	SendEvent("432TTT-23{enter}")           ; acctNum of Customer to delete. (Valid.)
	SendEvent("3{enter}")                   ; Change Customer.name.
	SendEvent("432TTT-23{enter}")           ; acctNum of Customer element to change. (Invalid, we just deleted it.)
	SendEvent("3{enter}")                   ; Change Customer.name. (Need to pick again b/c reprompting forever is insane.)
	SendEvent("111GRE-98{enter}")           ; acctNum of Customer element to change. (Valid).
	SendEvent("Pele the Greatest{enter}")   ; New Customer.name.
	SendEvent("6{enter}")                   ; Return to main menu. (Prompt, since we haven't saved our changes yet.)
	SendEvent("g{enter}")                   ; Invalid Y/N prompt response.
	SendEvent("n{enter}")                   ; Stay in Customer Update submenu.
	SendEvent("4{enter}")                   ; Save CustomerData changes to file.
	SendEvent("txt/newfile.txt{enter}")     ; Filename. (Linux.)
	SendEvent("6{enter}")                   ; Return to main menu. (No prompt this time, since we saved our changes somewhere.)
	SendEvent("6{enter}")                   ; Customer Purchase. (Updated for Lab 3, so the behavior's different.)
	SendEvent("123QWE-12{enter}")           ; Customer.acctNum. (Exists in CustomerData, so we don't prompt for a name.)
	SendEvent("2{enter}")                   ; Number of TVs they're buying.
	SendEvent("6{enter}")                   ; Customer Purchase. (Updated for Lab 3, so the behavior's different.)
	SendEvent("none{enter}")                ; "none" as the acctNum. (Lab expects this to be special-cased, sort of.)
	SendEvent("Marie Curie{enter}")         ; New Customer's name.
	SendEvent("090RAD-45{enter}")           ; New Customer's acctNum.
	SendEvent("3{enter}")                   ; Number of TVs they're buying.
	SendEvent("9{enter}")                   ; End program. (Fails, because we still have to check out the new Customer.)
	SendEvent("7{enter}")                   ; Checkout the (second-to-last) Customer in the Queue.
	SendEvent("7{enter}")                   ; Checkout the (last) Customer in the Queue.
	SendEvent("9{enter}")                   ; End program. (Prompts, because we mutated CustomerData without saving.)
	SendEvent("g{enter}")                   ; Invalid Y/N prompt answer.
	SendEvent("n{enter}")                   ; Return to main menu.
	SendEvent("5{enter}")                   ; Back to the Customer Update submenu.
	SendEvent("4{enter}")                   ; Save CustomerData changes to file.
	SendEvent("txt/newfile.txt{enter}")     ; Filename. (Linux.)
	SendEvent("6{enter}")                   ; Return to main menu. (No prompt.)
	SendEvent("9{enter}"                    ; End program. (No prompt this time, since we saved our changes somewhere.)
)

}               ; Enter test keystrokes with:   Ctrl + Alt + Shift + T

Pause::Pause -1 ; Pause & resume script with:   Pause/Break

^!+r::Reload    ; Reload script with:           Ctrl + Alt + Shift + R
