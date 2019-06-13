function[]=ReturnBestSolution(solution,Nant,Pdur,Pcost,Pover)

disp('            PROJECT STATS            ');

disp('Project Duration');
Pdur(Nant)
disp('Project Cost');
Pcost(Nant)
disp('Project Overwork');
Pover(Nant)

disp('employees dedication to this project tasks/day');
disp('x8 hrs');
disp(solution(Nant).ant);
end
