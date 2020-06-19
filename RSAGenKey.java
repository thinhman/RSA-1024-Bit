import java.math.BigInteger;
import java.util.BitSet;
import java.util.Random;

public class RSAGenKey {
    private static final BigInteger publicKeyExponent = new BigInteger("65537");    //e

    public static void main(String[] args) {

        BigInteger randPrimeOne;    //p
        BigInteger randPrimeTwo;    //q
        BigInteger keyModulus;      //n
        BigInteger phiN;

        do{
            randPrimeOne = generateRandPrime(512);
            randPrimeTwo = generateRandPrime(512);
            keyModulus = randPrimeOne.multiply(randPrimeTwo);
            phiN = randPrimeOne.subtract(BigInteger.ONE).multiply(randPrimeTwo.subtract(BigInteger.ONE));
        }while(!isCoPrime(phiN));

        //testing output:
        //System.out.println("e and phi(n) is coprime: " + isCoPrime(phiN));

        BigInteger privateKeyExponent;
        privateKeyExponent = publicKeyExponent.modInverse(phiN);

        //testing output:
        //System.out.println("n = " + keyModulus);
        //System.out.println("d = " + privateKeyExponent);

        try{
            FileIO.storeKeys(args[0], keyModulus, publicKeyExponent, privateKeyExponent);
            System.out.println("Key Generation Finished!");
        }catch (ArrayIndexOutOfBoundsException ex){
            System.err.println("No Key file path specified! Exiting.");
            System.err.println(ex);
        }

    }

    private static BigInteger generateRandomOdd(int bitLength){
        BitSet bitSet = new BitSet(bitLength);
        bitSet.set(0);
        bitSet.set(bitLength-1);

        BigInteger ensure_Odd_N_Magnitude = new BigInteger(bitSet.toByteArray());
        ensure_Odd_N_Magnitude.shiftRight(1);
        ensure_Odd_N_Magnitude.setBit(0);

        Random rand = new Random();
        BigInteger randomNumber = new BigInteger(bitLength, rand);
        randomNumber = randomNumber.or(ensure_Odd_N_Magnitude);

        return randomNumber;
    }

    private static BigInteger generateRandPrime(int bitLength){
        BigInteger randomOddInt;
        do {
            randomOddInt = generateRandomOdd(bitLength);
        } while (!MillerRabin_isPrime(randomOddInt));

        return randomOddInt;
    }

    private static boolean isCoPrime(BigInteger phiN){
        if(phiN.equals(publicKeyExponent))
            return false;

        return Steins_GCD(publicKeyExponent, phiN).equals(BigInteger.ONE);

    }

    //Steins Binary GCD Algorithm: Source Wikipedia
    private static BigInteger Steins_GCD(BigInteger u, BigInteger v){
        //gcd(0, v) = v, because everything divides zero, and v is the largest number that divides v.
        //Similarly, gcd(u, 0) = u. gcd(0, 0) is not typically defined,
        //but it is convenient to set gcd(0, 0) = 0.
        if(u.equals(BigInteger.ZERO))
            return v;
        if(v.equals(BigInteger.ZERO))
            return u;

        //If u and v are both even, then gcd(u, v) = 2·gcd(u/2, v/2), because 2 is a common divisor.
        int exponentOfTwo = 0;
        while(u.mod(BigInteger.TWO).equals(BigInteger.ZERO) && v.mod(BigInteger.TWO).equals(BigInteger.ZERO)){
            u = u.shiftRight(1);
            v = v.shiftRight(1);
            exponentOfTwo++;
        }

        do {
            //f u and v are both odd, and u ≥ v, then gcd(u, v) = gcd((u − v)/2, v).
            //If both are odd and u < v, then gcd(u, v) = gcd((v − u)/2, u)
            if(!u.mod(BigInteger.TWO).equals(BigInteger.ZERO) && !v.mod(BigInteger.TWO).equals(BigInteger.ZERO)){
                if(u.compareTo(v) > -1)
                    u = u.subtract(v).shiftRight(1);
                else
                    v = v.subtract(u).shiftRight(1);

            }

            //If u is even and v is odd, then gcd(u, v) = gcd(u/2, v), because 2 is not a common divisor.
            //Similarly, if u is odd and v is even, then gcd(u, v) = gcd(u, v/2).
            if(u.mod(BigInteger.TWO).equals(BigInteger.ZERO))
                u = u.shiftRight(1);
            else if (v.mod(BigInteger.TWO).equals(BigInteger.ZERO))
                v = v.shiftRight(1);


        } while (!u.equals(v) && !u.equals(BigInteger.ZERO));
        //Repeat steps until u = v, or (one more step) until u = 0.
        //In either case, the GCD is (2^k)*v, where k is the number of common factors of 2.

        return BigInteger.TWO.pow(exponentOfTwo).multiply(v);
    }

    /* Source: Wikipedia Miller-Rabin Algorithm
    Input #1: n > 3, an odd integer to be tested for primality
    Input #2: k, the number of rounds of testing to perform
    Output: "composite" if n is found to be composite, "probably prime" otherwise

    write n as 2r*d + 1 with d odd (by factoring out powers of 2 from n − 1)
    WitnessLoop: repeat k times:
        pick a random integer a in the range [2, n − 2]
        x <- ad mod n
        if x = 1 or x = n − 1 then
            continue WitnessLoop
        repeat r − 1 times:
            x <- x2 mod n
            if x = n − 1 then
                continue WitnessLoop
        return "composite"
    return "probably prime"*/

    private static boolean MillerRabin_isPrime(BigInteger randomOdd){
        if(randomOdd.equals(BigInteger.TWO) || randomOdd.equals(BigInteger.TWO.add(BigInteger.ONE)))
            return true;
        if(randomOdd.mod(BigInteger.TWO).equals(BigInteger.ZERO))
            return false;

        BigInteger nMinusOne = randomOdd.subtract(BigInteger.ONE);
        int bitLength = nMinusOne.bitLength();
        int exponent = 0;

        while(nMinusOne.mod(BigInteger.TWO).equals(BigInteger.ZERO)){
            exponent++;
            nMinusOne = nMinusOne.shiftRight(1);
        }

        for(int k = 0; k < 4; k++){
            BigInteger a = new BigInteger(bitLength, new Random());     //a = rand # from [0, n-1]
            while(a.compareTo(BigInteger.TWO) < 0){
                a = new BigInteger(bitLength, new Random());
            }   //a = rand # from[2, n-1]

            BigInteger checkPrime = a.modPow(nMinusOne, randomOdd);
            int r = 0;

            if(checkPrime.equals(BigInteger.ONE) || checkPrime.equals(randomOdd.subtract(BigInteger.ONE)))
                continue;

            for(; r < exponent-1; r++){
                checkPrime = checkPrime.pow(2).mod(randomOdd);

                if(checkPrime.equals(BigInteger.ONE))
                    return false;
                if(checkPrime.equals(randomOdd.subtract(BigInteger.ONE)))
                    break;
            }

            if(r == exponent-1)
                return false;
        }
        return true;
    }

}
