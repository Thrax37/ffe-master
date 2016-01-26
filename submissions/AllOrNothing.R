args <- strsplit(commandArgs(TRUE),";")[[1]]
round <- as.integer(args[1])
me <- as.integer(args[2])
stats <- do.call(rbind,strsplit(args[-(1:2)],"_"))
stats <- as.data.frame(apply(stats,2,as.integer))
colnames(stats) <- c("id","Sane","Infected","Dead","InfRate","ContRate","LethRate","MigRate")
out <- ""
statme <- stats[stats$id==me,]
while(nchar(out)<3){
    if(round==1 & !nchar(out)){
        out <- paste0(out, "B")
    }else if(round%%5==4 & statme$Infected > 20){
        statme$Infected <- statme$Infected - 30
        out <- paste0(out, "Q")
    }else if(statme$Sane*statme$InfRate/100 >= 1){
        o <- ifelse(statme$Sane*statme$InfRate/100 < statme$Infected*statme$ContRate/100, "C", "M")
        if(o=="C") statme$Infected <- statme$Infected - 10
        if(o=="M") statme$InfRate <- statme$InfRate - 4
        out <- paste0(out, o)
    }else if(statme$Infected > 0){
        statme$Infected <- statme$Infected - 10
        out <- paste0(out, "C")
    }else if(median(stats$LethRate)<20){ 
        out <- paste0(out, "W")
    }else{
        out <- paste0(out, "E")     
    }
}
cat(substr(out,1,3))