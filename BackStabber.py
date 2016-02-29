from sys import argv
round=int(argv[1].split(";")[0])
if round==1:
    print("VPO")
elif round<11:
    print("VCO")
elif round<23:
    print("VCC")
elif round<35:
    print("VCB")
elif round<41:
    print("VPB")
elif round<46:
    print("CTT")
else:
    print("TTT")