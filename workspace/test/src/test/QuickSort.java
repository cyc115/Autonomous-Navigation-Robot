package test;

public class QuickSort {
	static int swaps = 0 ;
	
	public static void main(String[] args) {
		int [] a = {3,8,2,6,5,9,6,2,1,5};
	
		print(a);
		
		quickSort(a, 0, a.length-1);
		
		print(a);
		System.out.println(swaps);
		
	}
	
	private static void quickSort(int [] arr , int low , int high ){
		if (low > high){
			return ;
		}
		int l = low  ;
		int p = arr[low];
	
		int r = high ;
		boolean decrease = true ;
		

		while (l != r){
			if (decrease){
				if (arr[r] < p){
					arr[l] = arr[r] ;
					l++;
					decrease = false ;
				}
				else r--;
			}
			else {
				if (arr[l] > p){
					arr[r] = arr[l] ;
					r--;
					decrease = true ;
				}
				else l++;
			}
		}
		arr[l] = p ;
		swaps++;
		quickSort(arr, low, l-1);
		quickSort(arr, l+1, high);
	}

	private static void print(int [] arr){
		for (int v : arr){
			System.out.print(" " + v);
		}
		System.out.println();
	}
	
	private static void exchange (int i , int j,int [] numbers){
		numbers[i] ^= numbers[j];
		numbers[j] ^= numbers[i];
		numbers[i] ^= numbers[j];
	}

}
