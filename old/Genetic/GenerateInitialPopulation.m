
function [solution]=GenerateInitialPopulation(Nant,employee,empnum,task,tasknum)
%if (t==1)
solution = struct([]);
for k=1:1:Nant
    for i=1:1:empnum
        for j=1:1:tasknum
          if ((length(unique(employee(i).skill)))>=(length(unique(task(j).skill))))
              a = 0; %Leveling
              b1 = employee(i).max;
              solution(k).ant(i,j)= (b1-a).*rand + a;%Xij for the fitness function
          else
              solution(k).ant(i,j)=0;
          end
        end
    end
end
end

