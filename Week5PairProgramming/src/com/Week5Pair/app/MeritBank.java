package com.Week5Pair.app;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.text.ParseException;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public class MeritBank {
//	private static SavingsAccount savingsAcc[] = new SavingsAccount[0];
//	private static CDOffering checkAcc[] = new CDOffering[0];
	private static CDOffering CDoff[] = new CDOffering[0];
	private static AccountHolder AHNewaccounts[] = new AccountHolder[0];
//	private static CDOffering[] cdOfferings;
//	double totalBalance = 0;
//	public static CDOffering bestCDOffering;
//	static CDOffering secondBestCDOffering;
	static long nextAccountNumber = 0;

	public static void addAccountHolder(AccountHolder accountHolder) {
//		int test = 0;
//		int incr = test + 1;
		AccountHolder[] testArray = new AccountHolder[AHNewaccounts.length + 1];
		for (int i = 0; i < testArray.length - 1; i++) {
			testArray[i] = AHNewaccounts[i];
		}
		AHNewaccounts = testArray;
		AHNewaccounts[AHNewaccounts.length - 1] = accountHolder;
//		test++;
	}

	public static AccountHolder[] getAccountHolders() {
		return AHNewaccounts;
	}

	public static CDOffering[] getCDOfferings() {
		return CDoff;
	}

	public static CDOffering getBestCDOffering(double depositAmount) {
		double best = 0.0;
		CDOffering bestOffering = null;
		if (CDoff == null) {
			return null;
		}
		for (CDOffering offering : CDoff) {
			if (futureValue(depositAmount, offering.getInterestRate(), offering.getTerm()) > best) {
				bestOffering = offering;
				best = futureValue(depositAmount, offering.getInterestRate(), offering.getTerm());
			}
		}
		return bestOffering;
	}

	public static CDOffering getSecondBestCDOffering(double depositAmount) {
		if (CDoff == null) {
			return null;
		}
		CDOffering bestOffering = null;
		double best = 0.0;
		CDOffering secondBestOffering = null;

		for (CDOffering offering : CDoff) {
			if (futureValue(depositAmount, offering.getInterestRate(), offering.getTerm()) > best) {
				secondBestOffering = bestOffering;
				bestOffering = offering;
				best = futureValue(depositAmount, offering.getInterestRate(), offering.getTerm());
			}
		}
		return secondBestOffering;
	}

	public static void clearCDOfferings() {
		CDoff = null;
	}

	public static void setCDOfferings(CDOffering[] offerings) {
		CDoff = offerings;
	}

	public static long getNextAccountNumber() {
		return nextAccountNumber;
	}

	public static double totalBalances() {
		double total = 0.0;
		for (AccountHolder accounts : AHNewaccounts) {
			total += accounts.getCombinedBalance();
		}
		return total;

	}

	public static double futureValue(double presentValue, double interestRate, int term) {

		// double futureValueM = presentValue * Math.pow(1 + interestRate, term);
		return recursiveFutureValue(presentValue, term, interestRate);
	}

	public static double recursiveFutureValue(double amount, int years, double interestRate) {
		if (years > 0) {
			double newAmount = amount + (amount * interestRate);
			return recursiveFutureValue(newAmount, years - 1, interestRate);
		}
		return amount;
	}

	static boolean readFromFile(String fileName)
			throws ExceedsFraudSuspicionLimitException, NegativeAmountException, ExceedsCombinedBalanceLimitException {
		CDOffering larry[] = new CDOffering[0];
		new FraudQueue();
		new HashSet<String>();

		try {
			FileReader file = new FileReader(fileName);
			BufferedReader bufferedReader = new BufferedReader(file);
			Long nextAccountNumber = Long.valueOf(bufferedReader.readLine());
			int offeringNumber = Integer.valueOf(bufferedReader.readLine());
			for (int i = 0; i < offeringNumber; i++) {
				larry = Arrays.copyOf(larry, larry.length + 1);
				larry[larry.length - 1] = CDOffering.readFromString(bufferedReader.readLine());
			}
			int accountHolderNumber = Integer.valueOf(bufferedReader.readLine());
			AccountHolder[] newAccountHoldersArr = new AccountHolder[accountHolderNumber];
			for (int a = 0; a < accountHolderNumber; a++) {
				AccountHolder accHolder = AccountHolder.readFromString(bufferedReader.readLine());
				int checkingAccNum = Integer.valueOf(bufferedReader.readLine());
				for (int b = 0; b < checkingAccNum; b++) {
					accHolder.addCheckingAccount(CheckingAccount.readFromString(bufferedReader.readLine()));
				}
				int savingAccNum = Integer.valueOf(bufferedReader.readLine());
				for (int c = 0; c < savingAccNum; c++) {
					accHolder.addSavingsAccount(SavingsAccount.readFromString(bufferedReader.readLine()));
				}
				int cdAccNum = Integer.valueOf(bufferedReader.readLine());
				for (int x = 0; x < cdAccNum; x++) {
					accHolder.addCDAccount(CDAccount.readFromString(bufferedReader.readLine()));
				}
				newAccountHoldersArr[a] = accHolder;
			}
			setNextAccountNumber(nextAccountNumber);
			CDoff = larry;
			AHNewaccounts = newAccountHoldersArr;
			file.close();
			return true;
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			return false;
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		} catch (ParseException e) {
			e.printStackTrace();
			return false;
		} catch (NumberFormatException e) {
			e.printStackTrace();
			return false;
		}

	}

	static boolean writeToFile(String fileName) {
		
		try {
			FileWriter fileOut = new FileWriter(fileName);
			BufferedWriter bufferedWriter = new BufferedWriter(fileOut);
			bufferedWriter.write(String.valueOf(nextAccountNumber));
			bufferedWriter.newLine();
			bufferedWriter.write(String.valueOf(CDoff.length));
			bufferedWriter.newLine();
			for (int i = 0; i < CDoff.length; i++) {
				bufferedWriter.write(CDoff[i].writeToString());
				bufferedWriter.newLine();
			}

			bufferedWriter.write(String.valueOf(AHNewaccounts.length));
			bufferedWriter.newLine();

			for (int a = 0; a < AHNewaccounts.length; a++) {
				bufferedWriter.write(AHNewaccounts[a].writeToString());
				bufferedWriter.newLine();
				bufferedWriter.write(String.valueOf(AHNewaccounts[a].getCheckingAccounts().length));
				bufferedWriter.newLine();
				for (int b = 0; b < AHNewaccounts[a].getCheckingAccounts().length; b++) {
					bufferedWriter.write(AHNewaccounts[a].getCheckingAccounts()[b].writeToString());
					bufferedWriter.newLine();
				}

				bufferedWriter.write(String.valueOf(AHNewaccounts[a].getSavingsAccounts().length));
				bufferedWriter.newLine();
				for (int x = 0; x < AHNewaccounts[a].getSavingsAccounts().length; x++) {
					bufferedWriter.write(AHNewaccounts[a].getSavingsAccounts()[x].writeToString());
					bufferedWriter.newLine();
				}

				bufferedWriter.write(String.valueOf(AHNewaccounts[a].getCDAccounts().length));
				bufferedWriter.newLine();
				for (int n = 0; n < AHNewaccounts[a].getCDAccounts().length; n++) {
					bufferedWriter.write(AHNewaccounts[a].getCDAccounts()[n].writeToString());
					bufferedWriter.newLine();
				}
			}
			fileOut.close();
			return true;
		} catch (Exception e) {
			return false;
		}

	}

	public static boolean processTransaction(Transaction transaction)
			throws NegativeAmountException, ExceedsAvailableBalanceException, ExceedsFraudSuspicionLimitException {
		BankAccount source = transaction.getSourceAccount();
		BankAccount target = transaction.getTargetAccount();

		if (source == null) {
			if (transaction instanceof WithdrawTransaction) {
				if (transaction.getAmount() < 0) {
					throw new NegativeAmountException();
				}
				if (transaction.getAmount() + target.getBalance() < 0) {
					throw new ExceedsAvailableBalanceException();
				}
				if (transaction.getAmount() < -1000) {
					FraudQueue.addTransaction(transaction);
					throw new ExceedsFraudSuspicionLimitException(
							"Transaction exceeds $1000.00 and must be reviewed prior to processing");
				}

				return true;
			}
			if (transaction.getAmount() < 0) {
				throw new NegativeAmountException();
			}
			if (transaction.getAmount() > 1000) {
				FraudQueue.addTransaction(transaction);
				throw new ExceedsFraudSuspicionLimitException(
						"Transaction exceeds $1000.00 and must be reviewed prior to processing");
			}
			return true;
		}
		if (source.getBalance() < transaction.getAmount()) {
			throw new ExceedsAvailableBalanceException();
		}
		if (transaction.getAmount() < 0) {
			throw new NegativeAmountException();
		}
		if (transaction.getAmount() > 1000) {
			FraudQueue.addTransaction(transaction);
			throw new ExceedsFraudSuspicionLimitException(
					"Transaction exceeds $1000.00 and must be reviewed prior to processing");
		} else {
			source.withdraw(transaction.amount);
			target.deposit(transaction.amount);

		}
		return true;

	}

	public static FraudQueue getFraudQueue() {
		return getFraudQueue();
	}

	static AccountHolder[] sortAccountHolders() {
		Arrays.sort(AHNewaccounts);
		for (AccountHolder modify : AHNewaccounts) {
			System.out.println("Modified arr[] " + modify);

		}
		return AHNewaccounts;
	}

	static void setNextAccountNumber(long accountNumber) {
		nextAccountNumber = accountNumber;

	}

	public static BankAccount getBankAccount(Long accounts) {

		return null;
	}

}
