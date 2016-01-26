input=arg[1]

datas={}
format={"playerID","sane","infected","dead","infection","contagion","lethality","migration"}
i=1
for s in input:gmatch("[^;]+") 
do
  j=1
  if round==nil then round=tonumber(s) 
  elseif me==nil then me=tonumber(s)+1
  else
    table.insert(datas,{})
    for r in s:gmatch("%d+")
    do
      datas[i][format[j]]=tonumber(r)
      j=j+1
    end
    i=i+1
  end
end
for i=#datas-1,1,-1
do
  for j=1,i
  do
    if datas[j].playerID>datas[j+1].playerID
    then
      datas[j],datas[j+1]=datas[j+1],datas[j]
    end
  end
end

-- First, we put ourself in a safe state
if round==1 then print("VVV")os.exit(0)end
if round==50 then print("CCC")os.exit(0)end

actions=""

-- Safety actions first
if datas[me].lethality>2 
then 
  actions=actions.."I"
  datas[me].lethality=datas[me].lethality-4>0 and datas[me].lethality-4 or 0
end

if datas[me].infected>=10
then
  if(datas[me].infection+datas[me].contagion+datas[me].lethality>4)
  then
    actions=actions.."V"
    datas[me].infection=datas[me].infection-1>0 and datas[me].infection-1 or 0
    datas[me].contagion=datas[me].contagion-4>0 and datas[me].contagion-4 or 0
    datas[me].lethality=datas[me].lethality-2>0 and datas[me].lethality-2 or 0
  end
  actions=actions.."C"
  datas[me].sane=datas[me].sane+10
  datas[me].infected=datas[me].infected-10
end

-- We can now try taking some initiatives
while #actions<3
do
  rates={}
  for i=1,#datas
  do
    if i~=me 
    then
      table.insert(rates,(datas[i].infected/datas[i].sane>0 and datas[i].sane or 0)*(datas[i].migration/100))
    end
  end
  rates["total"]=0
  for i=1,#rates
  do
    rates.total=rates.total+rates[i]
  end
  rates.total=(rates.total/#rates)*100


  if datas[me].migration<=15 and datas[me].migration+10>rates.total
  then
    actions=actions.."O"
    datas[me].migration=datas[me].migration+10>0 and datas[me].migration+10 or 0
  elseif (datas[me].sane/datas[me].infected)*100<rates.total
  then
    actions=actions.."B"
    datas[me].migration=datas[me].migration-10>0 and datas[me].migration-10 or 0
  elseif datas[me].infected>=10
  then
    actions=actions.."C"
    datas[me].infected=datas[me].infected-10
  else
    actions=actions.."V"
    datas[me].infection=datas[me].infection-1>0 and datas[me].infection-1 or 0
    datas[me].contagion=datas[me].contagion-4>0 and datas[me].contagion-4 or 0
    datas[me].lethality=datas[me].lethality-2>0 and datas[me].lethality-2 or 0
  end
end
print(actions)
os.exit(0)