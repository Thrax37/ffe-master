from sys import argv 

data=argv[1].split(";")[2].split("_")
inf=data[2]
hel=data[1]
dead=data[3]
if inf>29:
    print("CQM") # if there is to many sick cure some, quarantine some, and try to get rid of the disease  
else:
    if hel<80:
        print("BMV") # if there's few healthy, close our borders, because it's other countries
    else:
        if dead>19:
            print("WMD") # if we have too many dead attack others, but cure ourselves
        else:
            print("TWD") # if none of these are true we want to kill everyone else