clear all;
close all;
numberOfPoints=31;
numberOfPointsCDF=81;
throughputPRB=zeros(8,numberOfPoints);
distance=zeros(1,numberOfPoints);
throughput=zeros(8,numberOfPoints);
snr=zeros(8,numberOfPoints);
throughputPRBMean=zeros(8,1);
throughputMean=zeros(8,1);
snrMean=zeros(8,1);
throughputSum=zeros(8,1);
numberOfPRBs=zeros(8,numberOfPoints);
PRBSum=zeros(8,1);
SNRCDF=zeros(1,numberOfPointsCDF);
throughputCDF=zeros(1,numberOfPointsCDF);
probabilitySNR=zeros(8,numberOfPointsCDF);
probabilityThroughput=zeros(8,numberOfPointsCDF);


for p=1:7
    if p==1
        datoteka='reuse1.000-3.txt';
    elseif p==2
        datoteka='reuse3.000-3.txt';
    elseif p==3
        datoteka='a70b100g0d50ir50iir0.txt';
    elseif p==4
        datoteka='a70b100g0d50ir150iir0.txt';
    elseif p==5
        datoteka='a70b100g0d50ir250iir0.txt';
    elseif p==6
        datoteka='a70b100g0d50ir350iir0.txt';
    elseif p==7
        datoteka='a70b100g0d50ir400iir0.txt';
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

helper=zeros(8,numberOfPoints);
for i=1:numberOfPoints
    helper(1,i)=snrMean(1,1);
    helper(2,i)=snrMean(2,1);
    helper(3,i)=snrMean(3,1);
    helper(4,i)=snrMean(4,1);
    helper(5,i)=snrMean(5,1);
    helper(6,i)=snrMean(6,1);
    helper(7,i)=snrMean(7,1);
end;

figure('Name','LTE simulation SNR','numbertitle','off');

plot(distance(1,:),snr(1,:),'-rd',...
  distance(1,:),snr(2,:),'-b*',...
  distance(1,:),snr(3,:),'-go',...
  distance(1,:),snr(4,:),'-k>',...
  distance(1,:),snr(5,:),'-mx',...
  distance(1,:),snr(6,:),'-cs',...
  distance(1,:),snr(7,:),'-hy',...
  distance(1,:),helper(1,:),'--r',...
  distance(1,:),helper(2,:),'--b',...
  distance(1,:),helper(3,:),'--g',...
  distance(1,:),helper(4,:),'--k',...
  distance(1,:),helper(5,:),'--m',...
  distance(1,:),helper(6,:),'--c',...
  distance(1,:),helper(7,:),'--y');
xlim([35 500]), xlabel('\itdistance [m]'),ylabel('\itSNR [dB]');
legend('SNR reuse 1/3','SNR reuse 1','SNR a70 b100 g0 d50 ir50','SNR a70 b100 g0 d50 ir150','SNR a70 b100 g0 d50 ir250','SNR a70 b100 g0 d50 ir350','SNR a70 b100 g0 d50 ir400',...
   'SNR mean reuse 1/3','SNR mean reuse 1','SNR mean a70 b100 g0 d50 ir50','SNR mean a70 b100 g0 d50 ir150','SNR mean a70 b100 g0 d50 ir250',...
   'SNR mean a70 b100 g0 d50 ir350','SNR mean a70 b100 g0 d50 ir400','Location','best');

for i=1:numberOfPoints
    helper(1,i)=throughputPRBMean(1,1);
    helper(2,i)=throughputPRBMean(2,1);
    helper(3,i)=throughputPRBMean(3,1);
    helper(4,i)=throughputPRBMean(4,1);
    helper(5,i)=throughputPRBMean(5,1);
    helper(6,i)=throughputPRBMean(6,1);
    helper(7,i)=throughputPRBMean(7,1);
end;

figure('Name','LTE simulation throughput per PRB','numbertitle','off');

plot(distance(1,:),throughputPRB(1,:),'-rd',distance(1,:),throughputPRB(2,:),'-b*',distance(1,:),throughputPRB(3,:),'-go',distance(1,:),throughputPRB(4,:),'-k>',distance(1,:),throughputPRB(5,:),'-mx',distance(1,:),throughputPRB(6,:),'-cs',distance(1,:),throughputPRB(7,:),'-yh');

xlim([35 500]), xlabel('\itdistance [m]'),ylabel('\itThroughput per PRB [kbps]');
legend('R3','R1', ...
   'ICB = 50','ICB = 150',...
   'ICB = 250','ICB = 350','ICB = 400','Location','best');

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
%legend('Throughput reuse 1/3','Throughput reuse 2/3','Throughput reuse 1',...
%   'Throughput mean reuse 1/3','Throughput mean reuse 2/3','Throughput mean reuse 1','Location','best');


%figure('Name','LTE simulation PRBs','numbertitle','off');
%hold on;
%plot(distance(1,:),numberOfPRBs(1,:),'-rd',distance(1,:),numberOfPRBs(2,:),'-go',distance(1,:),numberOfPRBs(3,:),'-b*',distance(1,:),numberOfPRBs(4,:),'-k>',distance(1,:),numberOfPRBs(5,:),'-mx',distance(1,:),numberOfPRBs(6,:),'-cs',distance(1,:),numberOfPRBs(7,:),'-yh');
%plot(distance(1,:),numberOfPRBs(8,:),'-p','Color',[.49 1 .63]);
%hold off;
%xlim([35 500]), xlabel('\itdistance [m]'),ylabel('\itPRB');
%legend('PRB reuse 1/3','PRB reuse 2/3','PRB reuse 1','PRB reuse 2.125/3',...
%   'PRB reuse 1.625/3','PRB reuse 1.250/3','PRB reuse 1.750/3','PRB reuse 2.500/3','Location','best');


for i=1:1
    helper(1,i)=throughputSum(1,1);
    helper(2,i)=throughputSum(2,1);
    helper(3,i)=throughputSum(3,1);  
    helper(4,i)=throughputSum(4,1); 
    helper(5,i)=throughputSum(5,1); 
    helper(6,i)=throughputSum(6,1); 
    helper(7,i)=throughputSum(7,1); 
end;

figure('Name','LTE simulation throughput sum','numbertitle','off');
hold on;
bar(1, helper(1,1)/1000,'r');
bar(2,helper(2,1)/1000,'b');
bar(3,helper(3,1)/1000,'g');
bar(4,helper(4,1)/1000,'k');
bar(5,helper(5,1)/1000,'m');
bar(6,helper(6,1)/1000,'c');
bar(7,helper(7,1)/1000,'y');

set(gca,'XTickLabel',{'';'';'';''});
xlim([0 8]),ylabel('\itThroughput sum [Mbps]');
legend('Throughput sum reuse 1/3','Throughput sum reuse 1', 'Throughput sum a70 b100 g0 d50 ir50','Throughput sum a70 b100 g0 d50 ir150',...
   'Throughput sum a70 b100 g0 d50 ir250','Throughput sum a70 b100 g0 d50 ir350','Throughput sum a70 b100 g0 d50 ir400',...
    'Location','best');
hold off;

figure('Name','LTE simulation SNR CDF','numbertitle','off');

plot(SNRCDF(1,:),probabilitySNR(1,:),'-rd',SNRCDF(1,:),probabilitySNR(2,:),'-b*',SNRCDF(1,:),probabilitySNR(3,:),'-go',SNRCDF(1,:),probabilitySNR(4,:),'-k>',SNRCDF(1,:),probabilitySNR(5,:),'-mx',SNRCDF(1,:),probabilitySNR(6,:),'-cs',SNRCDF(1,:),probabilitySNR(7,:),'-yh');
xlabel('\itSNR [dB]'),ylabel('\itProbability');
legend('Reuse 1/3','Reuse 1','a70 b100 g0 d50 ir50','a70 b100 g0 d50 ir150',...
   'a70 b100 g0 d50 ir250','a70 b100 g0 d50 ir350','a70 b100 g0 d50 ir400',...
   'Location','best');

figure('Name','LTE simulation throughput CDF','numbertitle','off');

plot(throughputCDF(1,:),probabilityThroughput(1,:),'-rd',throughputCDF(1,:),probabilityThroughput(2,:),'-b*',throughputCDF(1,:),probabilityThroughput(3,:),'-go',throughputCDF(1,:),probabilityThroughput(4,:),'-k>',throughputCDF(1,:),probabilityThroughput(5,:),'-mx',throughputCDF(1,:),probabilityThroughput(6,:),'-cs',throughputCDF(1,:),probabilityThroughput(7,:),'-yh');
xlabel('\itThroughput [kbps]'),ylabel('\itProbability');
legend('Reuse 1/3','Reuse 1','a70 b100 g0 d50 ir50','a70 b100 g0 d50 ir150',...
   'a70 b100 g0 d50 ir250','a70 b100 g0 d50 ir350','a70 b100 g0 d50 ir400',...
   'Location','best');

%for i=1:numberOfPoints
%    helper(1,i)=PRBSum(1,1);
%    helper(2,i)=PRBSum(2,1);
%    helper(3,i)=PRBSum(3,1);    
%end;

%figure('Name','LTE simulation PRB sum','numbertitle','off');
%plot(distance(1,:),helper(1,:),'-rd',distance(1,:),helper(2,:),'-go',distance(1,:),helper(3,:),'-b*');
%xlim([35 500]), ylabel('\itPRB sum');
%legend('PRB sum reuse 1/3','PRB sum reuse 2/3','PRB sum reuse 1',...
%   'Location','best');