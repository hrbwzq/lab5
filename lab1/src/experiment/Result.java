package experiment;

import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
/*this is git test!!!!*/


public class Result {
	///处理表达式，验证其输入是否合法，并且将连乘改为次幂，省略的*加上
	public static String dispose(String expression)
	{
		Pattern p =  Pattern.compile("^((([0-9]*[a-zA-Z]+\\^?[0-9]?)|[0-9]+)(\\s)*[\\+*-]?(\\s)*)+([0-9]*[a-zA-Z]+\\^?[0-9]?|[0-9]+)");
		Matcher m = p.matcher(expression);
		if(m.matches())
		{
			Pattern p1 = Pattern.compile("(([a-z])\\*)\\1*\\2");
			Matcher m1 = p1.matcher(expression);
			int count  = 0;
			while(m1.find()) {
		         count = (m1.end() - m1.start() + 1) / 2 ;
		         expression = expression.replace(m1.group(0), m1.group(2)+"^"+count);
		      }
			expression = expression.replaceAll("([0-9])(\\s)*([a-z]+)", "$1*$3");
			expression = expression.replaceAll("\\s*", "");
		}
		else
		{
			System.out.println("Illegal Input!");
			return "error";
		}
		return expression;
	}
	///合并同类项
	public static String merge(String expression)
	{
		PointLink[] polylist = new PointLink[10];
		for(int j=0;j<10;j++)
            polylist[j]=new PointLink();
		char[]op = new char[10];
		int x=0;
		for(int i=0;i<expression.length();i++)
		{
			if(expression.charAt(i) == '+')
			{
				op[x] = '+';
				x++;
			}
			else if(expression.charAt(i) == '-')
			{
				op[x] = '-';
				x++;
			}
		}
		///按加号拆分后将单项式存在事先构造好的数据结构里面
		String [] Mono = expression.split("\\+|-");
		for(int i=0;i<Mono.length;i++)
		{
			if(Mono[i].matches("[0-9]+.*"))
			{
				Pattern p = Pattern.compile("^[0-9]+");   
				Matcher m = p.matcher(Mono[i]);  
				m.lookingAt();
				int pre = Integer.parseInt(m.group());
				String str = Mono[i].replaceFirst(m.group(), "");
				polylist[i].setPointLink(pre,str);
			}
			else
			{
				polylist[i].setPointLink(1,"*"+Mono[i]);
				
			}
		}
		///将单项式里面的变量安字典序排序
		for(int i=0;i<Mono.length;i++)
		{
			
			String[] Sort = polylist[i].str.split("\\*");
			for(int j = 0;j<Sort.length;j++)
			{
				String temp=Sort[0];
		        for(int k=0;k<Sort.length-1;k++)
		        {
		            for(int l=k+1;l<Sort.length;l++)
		            {
		               if(Sort[k].compareTo(Sort[l])>0)
		               {
		            	   temp=Sort[k];
		            	   Sort[k]=Sort[l];
		            	   Sort[l]=temp;
		               }
		            }
		        }
			}
			String Temp = "";
			for(int j =0;j<Sort.length;j++)
			{
				if(j==Sort.length-1)
					Temp = Temp + Sort[j];
				else
					Temp  = Temp + Sort[j] + "*";
			}
			polylist[i].str = Temp;
		}
		Boolean flag = false;
		//按之前的加减号进行处理
		for(int i=0;i<Mono.length-1;i++)
		{
			for(int j=i+1;j<Mono.length;j++)
			{
				if(i!=0 && polylist[i].str.equals(polylist[j].str))
				{
					flag = true;
					if(op[j-1]=='+'&&op[i-1]=='+')
						polylist[i].pre = polylist[i].pre + polylist[j].pre;
					else if(op[j-1]=='+'&&op[i-1]=='-')
					{
						if(polylist[i].pre<0)
							polylist[i].pre = polylist[j].pre + polylist[i].pre;
						else
							polylist[i].pre = polylist[j].pre - polylist[i].pre;
						if(polylist[i].pre > 0)
						{
							op[i-1]='+';
						}
					}
						
					else if(op[j-1]=='-'&&op[i-1]=='+')
					{
						polylist[i].pre = polylist[i].pre - polylist[j].pre;
						if(polylist[i].pre < 0)
						{
							op[i-1]='-';
						}
					}
						
					else if(op[j-1]=='-'&&op[i-1]=='-')
					{
						if(polylist[i].pre<0)
							polylist[i].pre = polylist[i].pre - polylist[j].pre;
						else
							polylist[i].pre = 0-polylist[i].pre - polylist[j].pre;
						if(polylist[i].pre > 0)
						{
							op[i-1]='+';
						}
					}
					polylist[j].setPointLink(0,"  ");
					
				}
				else if(i==0 && polylist[i].str.equals(polylist[j].str))
				{
					flag = true;
					 if(op[j-1]=='+')
						 polylist[i].pre = polylist[i].pre + polylist[j].pre;
					 else
						 polylist[i].pre = polylist[i].pre - polylist[j].pre;
					 polylist[j].setPointLink(0,"  ");
				}
			}
			if(flag == false&&i!=0&&op[i-1]=='-')
				polylist[i].pre = 0-polylist[i].pre;
			if(i==Mono.length-2&&polylist[i+1].pre!=0&&op[i]=='-')
				polylist[i+1].pre = 0-polylist[i+1].pre;
			flag = false;
		}
		String result="";
		for(int i=0;i<Mono.length;i++)
		{
			if(polylist[i].pre!=0)
			{
				if(i!=0)
				{
					if(polylist[i].pre > 0)
						result = result + "+" + polylist[i].pre+polylist[i].str;
					else
						result = result + polylist[i].pre+polylist[i].str;
				}
				else
					result = result + polylist[i].pre+polylist[i].str;
			}
				
		}
		return result;
	} 
	//求导函数
	/**
	 * 
	 * @param str
	 * @param command
	 * @return
	 */
	public static String duff(String str,String command)//qiu dao 
	{
		int []biao=new int [1000];
		int w=0;
		int cishu,xishu;
		String[] ch1=command.split(" ");
		String ch=ch1[1];
		String result="";
		if(!str.contains(ch))
		{
			System.out.println("Error, no variable ");
			return "";
		}
		String[] StrArray = str.split("\\+|\\-");
		String[][] string= new String[StrArray.length][10];
		
		for(int i=0;i<str.length();i++)
		{
			if(str.charAt(i)=='+' )//|| str.charAt(i)=='-'
			{
				biao[w]=1;
				w++;
			}
			if(str.charAt(i)=='-')
			{
				biao[w]=-1;
				w++;
			}
		}		

        for(int i=0;i<StrArray.length;i++)
        	{
        		string[i]=StrArray[i].split("\\*");
        	}

        int k=0;
        for(int i=0;i<StrArray.length;i++)
        {
        	k=0;
        	for(int j=0;j<string[i].length;j++)
        	{
        		if(string[i][j].length() >= ch.length() )
        		{
        			if(string[i][j].substring(0,ch.length()).equals(ch))
        			{
        				k=1;
        				if(string[i][j].equals(ch))
        				{
        					cishu=1;
        				}
        					
        				else
        				{
            	    		cishu=Integer.parseInt(string[i][j].substring(ch.length()+1,string[i][j].length()));
            	    		cishu=Integer.valueOf(string[i][j].substring(ch.length()+1,string[i][j].length()));
        				}
        				String temp1=string[i][j];
        				string[i][j]=string[i][j].substring(0,ch.length())+"^"+String.valueOf(cishu-1);

        				if(ch.equals(string[i][j]) && j!=0)
        				{
        					string[i][j]="";
        					
        				}
        				if(ch.equals(string[i][j]) && j==0)
        				{
        					string[i][j]="1";
        				}

        				if(Character.isDigit(string[i][0].charAt(0)))
        				{
        					xishu=Integer.parseInt(string[i][0]);
        					xishu=Integer.valueOf(string[i][0]);
        					string[i][0]=String.valueOf(xishu*cishu);

        				}
        				else
        				{
        						if(cishu==1)
        							string[i][0]=string[i][0];
        						else
        							string[i][0]=String.valueOf(cishu)+"*"+string[i][0];
        				}
        				if(ch.equals(temp1) && j!=0)
        				{
        					string[i][j]="";
        					
        				}
        				if(ch.equals(temp1) && j==0)
        				{
        					string[i][j]="1";
        				}
        				if(cishu==2)
        				{
        					string[i][j]=string[i][j].substring(0,string[i][j].length()-2);
        				}  				
        			}        				
        		}
        	}
        	if(k==0)
        	{
        		for(int h=0;h<string[i].length;h++)
        			string[i][h]="";
        	}        		
        }
        String[] print=new String[string.length];
        for(int i=0;i<string.length;i++)
        {
        	for(int j=0;j<string[i].length;j++)
        	{
        		if(string[i][0].equals("") ||string[i][0].equals("	"))
        		{
        			break;
        		}	
        			
        		else
        		{
        			if(j==0)
        			{
        				
        				if(string[i][0]=="1")
        				{
        					if(1==string[i].length)
        						print[i]="1";
        					else
        						continue;
        				}
        				else
        				{
        					print[i]=string[i][0];
        				}
        			}
        			else
        			{
        				if(string[i][j].equals(""))
        					//break;
        					continue;
        				else if(print[i]==null)
        					print[i]=string[i][j];
        				else
        					print[i]=print[i]+"*"+string[i][j];
        			}
        		}        			
        	}        	
        }
        int temp=0;
        for(int i=0;i<print.length;i++)
        {
        	if(i==0 && print[i]!=null)
        	{
        		result+=print[i];
        		temp=1;
        	}
        		
        	else if(i!=0 && print[i]!=null)
        	{
        		
        		if(biao[i-1]==1)
        		{
        			if(temp==1)
        			{
        				result+="+"+print[i];
        				temp=1;
        			}
        			else
        			{
        				result+=print[i];
        				temp=1;
        			}
        		}
        			
        		else if(biao[i-1]==-1 && print[i]!=null)
        		{
        			if(temp==1)
        			{
        				result+="-"+print[i];
        				temp=1;
        			}
        			else
        			{
        				result+="-"+print[i];
        				temp=1;
        			}
        		}        			
        		else
        			break;
        	}
        	else if(print[i]==null)
        		continue;        		
        }
        if(temp==0)
        {
        	result+="0";
        }
        System.out.println(result);
		return result;
	}
	//求值函数(替换变量)
	public static String number(String str,String com)//��ֵ �滻 ���������� 
	{
		String str_old=str;	
		String[] com_Str = com.split(" ");

		String old=str;
		for(int i=0;i<com_Str.length;i++)
		{
			for(int j=0;j<com_Str[i].length();j++)
			{
				if(com_Str[i].charAt(j)=='=')
				{
					str=str.replace(com_Str[i].substring(0,j), com_Str[i].substring(j+1,com_Str[i].length()));
					if(str==old)
					{
						System.out.println(com_Str[i].substring(0,j)+" not exist");
					}
					else
						old=str;
									
				}
			}
		}
		String str2=result_(str);
		if(str2.equals("0"))
		{
			System.out.println(str2);
		}
		else
		{
			str2 = merge(str2);
			System.out.println(str2);
		}
		return str2;
		
	}
	//求值函数(具体求值)
	public static String result_ (String str)//���㲢����
	{
		String[] StrArray = str.split("\\+|\\-");
		String[][] string= new String[StrArray.length][10];
		
		int temp1;
		int temp2;
		int w=0;
		int []biao=new int [100];
		for(int i=0;i<str.length();i++)
		{
			if(str.charAt(i)=='+' )//|| str.charAt(i)=='-'
			{
				biao[w]=1;
				w++;
			}
			if(str.charAt(i)=='-')
			{
				biao[w]=-1;
				w++;
			}
		}
		for(int i=0;i<StrArray.length;i++)
		{
			string[i]=StrArray[i].split("\\*");
		}
		
		
		for(int i=0;i<StrArray.length;i++)
		{
			for(int j=0;j<string[i].length;j++)
			{
				for(int k=0;k<string[i][j].length();k++)
				{
					if(string[i][j].charAt(k)=='^')
					{
						if(Character.isDigit(string[i][j].charAt(k-1)))
						{
							temp1=Integer.parseInt(string[i][j].substring(0,k));
							temp1=Integer.valueOf(string[i][j].substring(0,k));
							temp2=Integer.parseInt(string[i][j].substring(k+1,string[i][j].length()));
							temp2=Integer.valueOf(string[i][j].substring(k+1,string[i][j].length()));
							temp1=(int) Math.pow(temp1,temp2);
							string[i][j]=String.valueOf(temp1);
						}

					}
				}
				String temp="";
				if(Character.isDigit(string[i][j].charAt(0)) && !Character.isDigit(string[i][0].charAt(0)))
				{
					temp=string[i][j];
					string[i][j]=string[i][0];
					string[i][0]=temp;
				}
				
			}
		}
		for(int i=0;i<string.length;i++)
		{
			for(int j=0;j<string[i].length;j++)
			{
				if(Character.isDigit(string[i][j].charAt(0)) && j!=0)
				{
					temp1=Integer.parseInt(string[i][j]);
					temp1=Integer.valueOf(string[i][j]);
					temp2=Integer.parseInt(string[i][0]);
					temp2=Integer.valueOf(string[i][0]);
					string[i][0]=String.valueOf(temp1*temp2);
					string[i][j]="";
				}
				
			}
			
		}
        String[] print=new String[string.length];
        for(int i=0;i<string.length;i++)
        {
        	for(int j=0;j<string[i].length;j++)
        	{
        		if(string[i][0].equals("") ||string[i][0].equals("	"))
        		{
        			break;
        		}	
        			
        		else
        		{
        			if(j==0)
        			{
        				
        				if(string[i][0].equals("1"))
        				{
        					if(1==string[i].length)
        						print[i]="1";
        					else
        						continue;
        				}
        				else
        				{
        					print[i]=string[i][0];
        				}
        			}
        			else
        			{
        				if(string[i][j].equals(""))
        					//break;
        					continue;
        				else if(print[i]==null)
        					print[i]=string[i][j];
        				else
        					print[i]=print[i]+"*"+string[i][j];
        			}
        		}        			
        	}        	
        }		
        String result="";
        int temp=0;
        for(int i=0;i<print.length;i++)
        {
        	if(i==0 && print[i]!=null)
        	{
        		result+=print[i];
        		temp=1;
        	}
        		
        	else if(i!=0 && print[i]!=null)
        	{
        		
        		if(biao[i-1]==1)
        		{
        			if(temp==1)
        			{
        				result+="+"+print[i];
        				temp=1;
        			}
        			else
        			{
        				result+=print[i];
        				temp=1;
        			}
        		}
        			
        		else if(biao[i-1]==-1 && print[i]!=null)
        		{
        			if(temp==1)
        			{
        				result+="-"+print[i];
        				temp=1;
        			}
        			else
        			{
        				result+="-"+print[i];
        				temp=1;
        			}
        		}        			
        		else
        			break;
        	}
        	else if(print[i]==null)
        		continue;        		
        }
        if(temp==0)
        {
        	result+="0";
        }
		return result;
	}
	public static void main( String[] args )
	{
		Scanner line = new Scanner(System.in);
		System.out.println("请输入表达式："); 
		String expression = line.nextLine();
		expression = dispose(expression);
		if(expression.equals("error"))
			return;
		expression = merge(expression);
		//System.out.println("化简后的表达式："+expression); 
		System.out.println("请输入命令："); 
		Scanner line2  = new Scanner(System.in);
		String command = line2.nextLine();
		String simplify = "!simplify";
		String derivation = "!d/d";
		
		while(!command.equals("exit"))
		{
			
			if(command.length()>8&&command.substring(0, 9).equals(simplify))
			{
				long startTime=System.currentTimeMillis();
				number(expression,command);
				long endTime=System.currentTimeMillis();
				System.out.println("程序开始时间： "+startTime+"ms");
				System.out.println("程序结束时间： "+endTime+"ms");
				System.out.println("程序运行时间： "+(endTime-startTime)+"ms");

			}
				
			else if(command.length()>3&&command.substring(0, 4).equals(derivation))
			{
				long startTime=System.currentTimeMillis();
				duff(expression,command);
				long endTime=System.currentTimeMillis();
				System.out.println("程序开始时间： "+startTime+"ms");
				System.out.println("程序结束时间： "+endTime+"ms");
				System.out.println("程序运行时间： "+(endTime-startTime)+"ms");
			}
				
			else
				System.out.println("Error,illegal command!");
			System.out.println("请输入命令(exit退出)："); 
			command = line2.nextLine();
		}
		
	}
}
