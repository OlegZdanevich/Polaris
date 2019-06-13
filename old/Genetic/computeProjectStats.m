function[]=computeProjectStats(filenameTask,filenameEmployee)
%%initialization%%
[G1,b,Nant,Ngen,arcnum,employee,empnum,empsknum,task,tasknum,tasksknum,wcost,wdur,wover,wpenal,wreqsk,wt_cost,wt_dur,wundt]=InitialInstances(filenameTask,filenameEmployee);
solution=GenerateInitialPopulation(Nant,employee,empnum,task,tasknum);
[CPM,CPMpath,G,Pcost,Pdur,Pover,allpths,solution,tpath]=Selection_EvolutionaryOperators(solution,G1,Nant,employee,empnum,task,tasknum);
%%Algorithm%%
t=1; 
while (t<=Ngen)
%%Fitness%%
Evaluate_Population=zeros(1,Nant);
Position=zeros(1,Nant);
for k=1:1:Nant
    Pover1=Pover(k);
    Pdur1=Pdur(k);
    Pcost1=Pcost(k);
    Evaluate_Population(k)=EvaluateObjectives(Pover1,Pdur1,Pcost1,wcost,wdur,wpenal,wundt,wreqsk,wover); 
    Position(k)=k;
end
%%Sorting Population(Selection)%%
IEv=[Evaluate_Population
     Position];
[Y,Index]=sort(IEv(1,:));
Best=IEv(:,Index); %use the column indices from sort() to sort all columns of IEv.
%%Mutation & Crossover%%
solution=UpdatePopulation(solution,Nant,employee,empnum,task,tasknum,Index);
%%Update Pareto Set%%
[CPM,CPMpath,G,Pcost,Pdur,Pover,allpths,solution,tpath]=Selection_EvolutionaryOperators(solution,G1,Nant,employee,empnum,task,tasknum);
t=t+1;
end
ReturnBestSolution(solution,Nant,Pdur,Pcost,Pover);
end

