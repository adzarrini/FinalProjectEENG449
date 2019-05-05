clear all; close all; clc;
A = imread('Lenna.png');
[rows, columns, colorBands] = size(A);
% if colorBands > 1 
%     B = A(:, :, 1);
% end
% F = fft2(A);
figure(1);
title('Original Image');
imshow(A);

% figure(2)
% title('Band 1');
% imshow(A(:,:,1));
% 
% figure(3)
% title('Band 2');
% imshow(A(:,:,2));
% 
% figure(4)
% title('Band 3');
% imshow(A(:,:,3));

G = rgb2gray(A);
figure(5)
imshow(G);
title('Gray Scale');

F = fft2(G);
% figure(6);
% title('FFT');
% imshow(F);

S = abs(F);
figure(7);
title('magnitude');
imshow(S,[]);

FCP = fftshift(S);
figure(8);
title('center spectrum');
imshow(FCP,[]);

logFCP = log(1+abs(FCP));
figure(8);
title('Log Transformed Image');
imshow(logFCP,[]);

K0 = 0.4;

% Application of low pass filter in reconstruction
%Image dimensions 
[N,M] = size(G); %[height, width]
% Sampling intervals 
dx = 1; 
dy = 1; 
%Characteristic wavelengths 
KX0 = (mod(1/2 + (0:(M-1))/M, 1) - 1/2); 
KX1 = KX0 * (2*pi/dx); 
KY0 = (mod(1/2 + (0:(N-1))/N, 1) - 1/2); 
KY1 = KY0 * (2*pi/dx); 
[KX,KY] = meshgrid(KX1,KY1); 
%Filter formulation 
lpf = (KX.*KX + KY.*KY < K0^2); 

figure(9);
title('Low pass filter');
imshow(lpf,[]);

newImg = lpf.*F;

Ff = newImg;
Ff = abs(Ff);
Ff = fftshift(Ff);
Ff = log(1+abs(Ff));
figure(10)
imshow(Ff, []);

% newImg = ifftshift(newImg);
newImg = ifft2(newImg);
figure(10);
title('Final Image');
imshow(newImg,[]);

