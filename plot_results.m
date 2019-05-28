close all;
data = csvread('data.csv');
tick = data(1,:);
temp = data(2,:);
black = data(3,:);
white = data(4,:);
soil = data(5,:);

figure;
subplot(2,1,1);
plot(tick, temp);
xlabel('Tick');
ylabel('Global Temperature (\circC)');

subplot(2,1,2);
plot(tick, black, tick, white, '--');
xlabel('Tick');
ylabel('Population');
legend('Black', 'White');

figure;
plot(tick, soil);
xlabel('Tick');
ylabel('Soil Quality');
