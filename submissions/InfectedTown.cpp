#include <iostream>
#include <string>

int main(int argc, char *argv[])
{
    std::string action;
    if(argv[1][0]=='1' && argv[1][1]==';') {
        // First turn
        action = "BII";
    } else if (argv[1][0]=='5' && argv[1][1]=='0'){
        // Last turn
        action = "CCC";
    } else {
        // All other turns
        action = "III";
    }
    std::cout<<action;
    return 0;
}