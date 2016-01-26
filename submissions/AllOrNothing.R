args <- strsplit(commandArgs(TRUE),";")[[1]]
round <- as.integer(args[1])
me <- as.integer(args[2])
stats <- do.call(rbind,strsplit(args[-(1:2)],"_"))
stats <- as.data.frame(apply(stats,2,as.integer))
colnames(stats) <- c("id","Sane","Infected","Dead","InfRate","ContRate","LethRate","MigRate")
out <- ""
statme <- stats[stats$id==me,]
while(nchar(out)<3){
    if(statme$Sane*statme$InfRate/100 > 1){
        statme$InfRate <- statme$InfRate - 4
        out <- paste0(out, "M")
    }else if(statme$Infected>10){
        statme$Infected <- statme$Infected - 10
        out <- paste0(out, "C")
    }else if(statme$Infected==0){
        out <- paste0(out, "B")
    }else if(median(stats$LethRate)<20){ 
        out <- paste0(out, "W")
    }else if(any(statme$Sane < stats$Sane)){
        out <- paste0(out, "T")
    }else{
        out <- paste0(out, "E")     
    }
    }
cat(substr(out,1,3))