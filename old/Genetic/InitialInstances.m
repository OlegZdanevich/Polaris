function[G1,b,Nant,Ngen,arcnum,employee,empnum,empsknum,task,tasknum,tasksknum,wcost,wdur,wover,wpenal,wreqsk,wt_cost,wt_dur,wundt]=InitialInstances(filenameTask,filenameEmployee)
tasknum=-1;                 %number of the tasks
empnum=-1;                  %number of the employees
empsknum=-1;                %number of the employees skills max
tasksknum=-1;               %number of the task skills max
arcnum=-1;                  %number of arc lines
graph=zeros(30);            %the graph of tasks
Ngen = 15;				    %Generation number
Nant = 400;				    %Number of ants in a generation
b = 1.1;                    %The importance rate of swarm exp.
wcost = 0.000001;			%The weight of cost
wdur = 0.1;                 %The weight of duration
wt_cost = 0.000001;			%The weight of task cost
wt_dur = 0.1;				%The weight of task duration
wover = 0.1;				%The weight of overwork
wpenal = 100;				%Penalty constant
wreqsk = 10;				%The weight of number of required skills
wundt =	10;                 %The weight of number of undone tasks
task = struct([]);%struct for the tasks
employee = struct([]);%struct for the employees
for lkk=1:1:30
    for skilp=1:1:10
        task(lkk).skill(skilp)=-1;
    end
    task(lkk).effort = 0.0;
end
for lkk=1:1:15
    for skilp=1:1:10
        employee(lkk).skill(skilp)=-1;
    end
    employee(lkk).max=0;
    employee(lkk).salary=0.0;
end

%project file
fid = fopen(filenameTask,'r'); %open instances
if fid<0, error('cannot open file %s\n'); end;
while ~feof(fid)
    line = strtrim(fgetl(fid));
    if (isempty(line) || all(isspace(line)) || strncmp(line,'#',1) || strncmp(line,';',1)||strncmp(line,',',1)); % no operation 
    else 
        [var,tok] = strtok(line,'.');
        [var1,tok1] = strtok(tok,'.');
        [var2,tok2] = strtok(tok1,'.');
        [var3,tok3] = strtok(tok2,'=');
        [var4,tok4] = strtok(tok3,'=');        
        line2=tok;
        [var1a,tok1a] = strtok(line2,'=');
        line3=tok1;
        [var2a,tok2a] = strtok(line3,'=');   
        line4=var2;
        [var3a,tok3a] = strtok(line4,' ');
        line5=tok1a;
        [zsf,zzs] = strtok(line5,'=');
        line6=var3;
        [zzzasd,zzzdsa] = strtok(line6,'.');
        line7 = tok3a;
        [klj,wsx]=strtok(line7,' ');
        [gr1,gr2] = strtok(var3a,'=');
        [gra1,gra2] = strtok(zsf,' ');
        %-----------------search-------------------------
        if (strcmp(var,'task')==1)
            if (strcmp(var1a,'.number')==1)
                tasknum=str2double(zsf);end
            if (strcmp(var2,'skill')==1)
                if(strcmp(var3,'.number')==1)
                    if(tasknum<str2double(var4)),tasksknum=str2double(var4);end
                    %task.7.skill.number 
                else
                task(str2double(var1)+1).skill(str2double(zzzasd)+1)=str2double(var4);
                %task.7.skill.1
                end
            end
            if(strcmp(var2a,'.cost')==1)
                task(str2double(var1)+1).effort=str2double(zsf);end
                %task.7.cost.1         
        end
        if (strcmp(var,'employee')==1)
            if (strcmp(var1a,'.number')==1)
                empnum=str2double(zsf); end
            if (strcmp(var2,'skill')==1);
                if(strcmp(var3,'.number')==1)
                    if (empsknum<str2double(var4)),empsknum=str2double(var4);end
                    %employee.7.skill.number
                else                
                employee(str2double(var1)+1).skill(str2double(zzzasd)+1)=str2double(var4);
                %p.x employee.7.skill.1
                end
            end
            if (strcmp(var2a,'.salary')==1);
                employee(str2double(var1)+1).salary = str2double(zsf);end
                %p.x employee.0.salary=12315.545465456            
        end
        if (strcmp(var,'graph')==1)
            if (strcmp(var1,'arc')==1)
                if(strcmp(var1a,'.arc.number')==1)
                    arcnum=str2double(zsf);
                else
                 grp1=str2double(klj);
                 grp2=str2double(gra1);
                 grp3=str2double(gr1);
                 graph(grp2+1,grp1+1)=1;
                end
            end
        end
    end    
end;
fclose(fid);

%dedication file
fid = fopen(filenameEmployee,'r');%open employees instances 
if fid<0, error('cannot open file %s\n',a); end;
while ~feof(fid)
    line = strtrim(fgetl(fid));
    if (isempty(line) || all(isspace(line)) || strncmp(line,'#',1) || strncmp(line,';',1)||strncmp(line,',',1)); % no operation 
    else
        [var,tok] = strtok(line,'.');
        [var1,tok1] = strtok(tok,'.');
        [var2,tok2] = strtok(tok1,'=');
        [var3,tok3] = strtok(tok2,'//');
        [var4,tok4] = strtok(var3,'=');
        if (str2double(var1)<=empnum)
            employee(str2double(var1)).max=str2double(var4);
        end
    end
end
fclose(fid);



G1=graph(1:tasknum,1:tasknum);

%Plot the Project Data
figure;
subplot(2,1,1); %task skills PLOT
y2 = zeros(tasknum,tasksknum);
for zfsk=1:1:tasknum
    for sakl=1:1:tasksknum
        if(task(zfsk).skill(sakl) == -1)
        y2(zfsk,sakl) = 0;    
        else    
        y2(zfsk,sakl) = task(zfsk).skill(sakl);
        end
    end   
end
bar(y2);
xlabel('Tasks');
ylabel('Skill');
hold on;

subplot(2,1,2); %employee skills PLOT
y1 = zeros(empnum,empsknum);
for zfsk=1:1:empnum
    for sakl=1:1:empsknum
        if(employee(zfsk).skill(sakl) == -1)
        y1(zfsk,sakl) = 0;    
        else    
        y1(zfsk,sakl) = employee(zfsk).skill(sakl);
        end
    end   
end
bar(y1);
xlabel('Employees');
ylabel('Skill');
hold on;

bg = biograph(G1,[],'ShowArrows','on','ShowWeights','off');
h = view(bg);
hold on;
end
