function [H] = lowpass(n)
% Matlab Program for 2D Low Pass Filter
[a,b] = freqspace(n,'meshgrid');
d = zeros(n,n);
for i = 1:n
    for j = 1:n
        d(i,j) = sqrt(a(i,j).^2 + b(i,j).^2);
    end
end
c = 0.5;
H = zeros(n,n);
for i = 1:n
    for j = 1:n
        if abs(d(i,j)) <= 0.5
            H(i,j) = 1;
        else
            H(i,j) = 0;
        end
    end
end
end

