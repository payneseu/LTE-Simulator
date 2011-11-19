clear all;
close all;
numberOfPoints=31;
numberOfPointsCDF=81;
throughputPRB=zeros(3,numberOfPoints);
distance=zeros(1,numberOfPoints);
throughput=zeros(3,numberOfPoints);
snr=zeros(3,numberOfPoints);
throughputPRBMean=zeros(3,1);
throughputMean=zeros(3,1);
snrMean=zeros(3,1);
throughputSum=zeros(3,1);
numberOfPRBs=zeros(3,numberOfPoints);
PRBSum=zeros(3,1);
SNRCDF=zeros(1,numberOfPointsCDF);
throughputCDF=zeros(1,numberOfPointsCDF);
probabilitySNR=zeros(3,numberOfPointsCDF);
probabilityThroughput=zeros(3,numberOfPointsCDF);


for p=1:3
    if p==1
        datoteka='reuse1.000-3.txt';
    elseif p==2
        datoteka='reuse3.000-3.txt';
    elseif p==3
        datoteka='a100b100g0d60ir300iir0.txt';
    end;
    dat=fopen(datoteka);
    
    %Distance
    textscan(dat,'%*s',1);
    if p==1
        for i=1:numberOfPoints
            s=textscan(dat,'%n',1);
            distance(p,i)=cell2mat(s);
        end;
    else 
       for i=1:numberOfPoints
            textscan(dat,'%*n',1);
       end; 
    end;
    
    %SNR
    textscan(dat,'%*s',1);
    for i=1:numberOfPoints
        s=textscan(dat,'%n',1);
        snr(p,i)=cell2mat(s);
    end;
    
    s=textscan(dat,'%*s %*s %n',1);
    snrMean(p,1)=cell2mat(s);
    
    %Throughput PRB
    textscan(dat,'%*s',3);
    for i=1:numberOfPoints
        s=textscan(dat,'%n',1);
        throughputPRB(p,i)=cell2mat(s);
    end;
    
    s=textscan(dat,'%*s %*s %*s %*s %n',1);
    throughputPRBMean(p,1)=cell2mat(s);
    
    %Throughput
    textscan(dat,'%*s',1);
    for i=1:numberOfPoints
        s=textscan(dat,'%n',1);
        throughput(p,i)=cell2mat(s);
    end;
    
    s=textscan(dat,'%*s %*s %n',1);
    throughputMean(p,1)=cell2mat(s);
    
    s=textscan(dat,'%*s %*s %n',1);
    throughputSum(p,1)=cell2mat(s);
    
    %Number od PRBs
    textscan(dat,'%*s %*s %*s',1);
    for i=1:numberOfPoints
        s=textscan(dat,'%n',1);
        numberOfPRBs(p,i)=cell2mat(s);
    end;
    
    s=textscan(dat,'%*s %*s %n',1);
    PRBSum(p,1)=cell2mat(s);
    
    %for CDF
    textscan(dat,'%*s %*s %*s',1);
    if p==1
        for i=1:numberOfPointsCDF
            s=textscan(dat,'%n',1);
            SNRCDF(1,i)=cell2mat(s);
        end;
    else
        for i=1:numberOfPointsCDF
            textscan(dat,'%n',1);
            
        end;
    end;
    
    textscan(dat,'%*s %*s %*s',1);
    for i=1:numberOfPointsCDF
        s=textscan(dat,'%n',1);
        probabilitySNR(p,i)=cell2mat(s);
    end;
    
    textscan(dat,'%*s %*s %*s',1);
    if p==1
        for i=1:numberOfPointsCDF
            s=textscan(dat,'%n',1);
            throughputCDF(1,i)=cell2mat(s);
        end;
    else
        for i=1:numberOfPointsCDF
            textscan(dat,'%n',1);            
        end;
    end;
    
    textscan(dat,'%*s %*s %*s',1);
    for i=1:numberOfPointsCDF
        s=textscan(dat,'%n',1);
        probabilityThroughput(p,i)=cell2mat(s);
    end;
    
    fclose(dat);
end;

helper=zeros(3,numberOfPoints);
for i=1:numberOfPoints
    helper(1,i)=snrMean(1,1);
    helper(2,i)=snrMean(2,1);
    helper(3,i)=snrMean(3,1);
end;

figure('Name','LTE simulation SNR','numbertitle','off');

plot(distance(1,:),snr(1,:),'-rd',...
  distance(1,:),snr(2,:),'-b*',...
  distance(1,:),snr(3,:),'-go',...
  distance(1,:),helper(1,:),'--r',...
  distance(1,:),helper(2,:),'--b',...
  distance(1,:),helper(3,:),'--g');
xlim([35 500]), xlabel('\itdistance [m]'),ylabel('\itSNR [dB]');
legend('SNR reuse 1/3','SNR reuse 1', 'SNR a100 b100 g0 d60 ir300',...
    'SNR mean reuse 1/3','SNR mean reuse 1', 'SNR a100 b100 g0 d60 ir300','Location','best');

for i=1:numberOfPoints
    helper(1,i)=throughputPRBMean(1,1);
    helper(2,i)=throughputPRBMean(2,1);
    helper(3,i)=throughputPRBMean(3,1);
end;

figure('Name','LTE simulation throughput per PRB','numbertitle','off');

plot(distance(1,:),throughputPRB(1,:),'-rd',distance(1,:),throughputPRB(2,:),'-b*',distance(1,:),throughputPRB(3,:),'-go');

xlim([35 500]), xlabel('\itdistance [m]'),ylabel('\itThroughput per PRB [kbps]');
legend('R3','R1', ...
    '\alpha = 100 \beta = 100 \delta = 60 ICB = 300','Location','best');

%for i=1:numberOfPoints
%    helper(1,i)=throughputMean(1,1);
%    helper(2,i)=throughputMean(2,1);
%    helper(3,i)=throughputMean(3,1);
%end;

%figure('Name','LTE simulation throughput','numbertitle','off');
%plot(distance(1,:),throughput(1,:),'-rd',distance(1,:),throughput(2,:),'-go',distance(1,:),throughput(3,:),'-b*',...
%  distance(1,:),helper(1,:),'--r',...
%  distance(1,:),helper(2,:),'--g',...
%  distance(1,:),helper(3,:),'--b');
%xlim([35 500]), xlabel('\itdistance [m]'),ylabel('\itThroughput [kbps]');
%legend('Throughput reuse 1/3','Throughput reuse 2/3', 'Throughput reuse 1',...
%    'Throughput mean reuse 1/3','Throughput mean reuse 2/3', 'Throughput mean reuse 1','Location','best');


%figure('Name','LTE simulation PRBs','numbertitle','off');
%hold on;
%plot(distance(1,:),numberOfPRBs(1,:),'-rd',distance(1,:),numberOfPRBs(2,:),'-go',distance(1,:),numberOfPRBs(3,:),'-b*',distance(1,:),numberOfPRBs(4,:),'-k>',distance(1,:),numberOfPRBs(5,:),'-mx',distance(1,:),numberOfPRBs(6,:),'-cs',distance(1,:),numberOfPRBs(7,:),'-yh');
%plot(distance(1,:),numberOfPRBs(8,:),'-p','Color',[.49 1 .63]);
%hold off;
%xlim([35 500]), xlabel('\itdistance [m]'),ylabel('\itPRB');
%legend('PRB reuse 1/3','PRB reuse 2/3', 'PRB reuse 1','PRB reuse 2.125/3',...
%    'PRB reuse 1.625/3','PRB reuse 1.250/3','PRB reuse 1.750/3','PRB reuse 2.500/3','Location','best');

for i=1:1
    helper(1,i)=throughputSum(1,1);
    helper(2,i)=throughputSum(2,1);
    helper(3,i)=throughputSum(3,1);  
end;

figure('Name','LTE simulation throughput sum','numbertitle','off');
hold on;
bar(1, helper(1,1)/1000,'r');
bar(2,helper(2,1)/1000,'b');
bar(3,helper(3,1)/1000,'g');
grid on;
set(gca,'XTickLabel',{'';'';''});
xlim([0 4]),ylabel('\itThroughput sum [Mbps]');
legend('Throughput sum reuse 1/3','Throughput sum reuse 1', 'Throughput sum a100 b100 g0 d60 ir300',...
    'Location','best');
hold off;


figure('Name','LTE simulation SNR CDF','numbertitle','off');

plot(SNRCDF(1,:),probabilitySNR(1,:),'-rd',SNRCDF(1,:),probabilitySNR(2,:),'-b*',SNRCDF(1,:),probabilitySNR(3,:),'-go');
xlabel('\itSNR [dB]'),ylabel('\itProbability');
legend('Reuse 1/3','Reuse 1', 'a100 b100 g0 d60 ir300',...
    'Location','best');

figure('Name','LTE simulation throughput CDF','numbertitle','off');

plot(throughputCDF(1,:),probabilityThroughput(1,:),'-rd',throughputCDF(1,:),probabilityThroughput(2,:),'-b*',throughputCDF(1,:),probabilityThroughput(3,:),'-go');
xlabel('\itThroughput [kbps]'),ylabel('\itProbability');
legend('Reuse 1/3','Reuse 1', 'a100 b100 g0 d60 ir300',...
    'Location','best');

%for i=1:numberOfPoints
%    helper(1,i)=PRBSum(1,1);
%    helper(2,i)=PRBSum(2,1);
%    helper(3,i)=PRBSum(3,1);    
%end;

%figure('Name','LTE simulation PRB sum','numbertitle','off');
%plot(distance(1,:),helper(1,:),'-rd',distance(1,:),helper(2,:),'-go',distance(1,:),helper(3,:),'-b*');
%xlim([35 500]), ylabel('\itPRB sum');
%legend('PRB sum reuse 1/3','PRB sum reuse 2/3', 'PRB sum reuse 1',...
%    'Location','best');