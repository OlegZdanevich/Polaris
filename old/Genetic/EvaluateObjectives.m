function [ f ] = EvaluateObjectives (Pover,Pdur,Pcost,wcost,wdur,wpenal,wundt,wreqsk,wover)


if Pover==0;
    Undt=0;Reqsk=0;
end
if ((Reqsk==0) && (Undt==0))		
	f = 1/(wcost*Pcost+wdur*Pdur);
else 
	f = 1/(wpenal+wundt*Undt+wreqsk*Reqsk+wover*Pover);	
end


end
