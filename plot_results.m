close all;
tempFile = fopen('temp.txt', 'r');
temp = fscanf(tempFile, '%f');
fclose(tempFile);
blackFile = fopen('black.txt', 'r');
black = fscanf(blackFile, '%f');
fclose(blackFile);
whiteFile = fopen('white.txt', 'r');
white = fscanf(whiteFile, '%f');
fclose(whiteFile);

len = length (temp);
tick = 1 : len;
figure;
plot(tick, temp);
xlabel('Tick');
ylabel('Global Temperature (\circC)');

figure;
plot(tick, black, tick, white, '--');
xlabel('Tick');
ylabel('Population');
legend('Black', 'White');
