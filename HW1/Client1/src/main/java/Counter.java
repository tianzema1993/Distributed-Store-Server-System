public class Counter {

  private int primes;

  public Counter() {
    this.primes = 0;
  }

  public void increasePrimeCount() {
    this.primes++;
  }

  public int getPrimes() {
    return primes;
  }
}
