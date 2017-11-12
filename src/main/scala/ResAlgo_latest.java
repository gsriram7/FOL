import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.HashMap;

class PredicateNameAndArgs{
	
	String predicateName;
	String[] arguments;
	
	PredicateNameAndArgs(){		
	}
	
	PredicateNameAndArgs(String predicateName, String args){		
		this.predicateName = predicateName;
		int i = 0;
		arguments = args.split(",");
		while(i<arguments.length){
			String str = arguments[i];
			char[] charArray = str.toCharArray();
			if(charArray[0]=='('){
				arguments[i] = str.substring(1);
				int indexOfClose = arguments[i].indexOf(')');
				if(indexOfClose == -1){
				}	
				else{
					arguments[i] = arguments[i].substring(0, indexOfClose);
				}
			}			
			else{
				if(str.endsWith(")")){	  
					int index = str.indexOf(')');
					arguments[i] = str.substring(0, index);
				}
			}
			i++;
		}		
	}
}

class KBRule{
	
    PredicateNameAndArgs predicateArray[];
	int selectedRule = -1;
	String splitRule;
	
	KBRule(){		
	}
	
	KBRule(String rule){
		String strArr[];
		splitRule = rule;
		strArr = splitRule.split("\\|");	
		String predName[] = new String[strArr.length];
		int i = 0;
		while(i < strArr.length){
			 int split = strArr[i].indexOf("(");
			 predName[i] = strArr[i].substring(0,split).trim();
			 strArr[i] = strArr[i].substring(split).trim();
			 i++;
		}
		
	    predicateArray = new PredicateNameAndArgs[strArr.length];
	    int j = 0;
	    while(j<strArr.length){
			predicateArray[j]=(new PredicateNameAndArgs(predName[j],strArr[j]));
			j++;
		}	
	}		
}

class Substitute{	
	KBRule rule;
	String variable;
	String constant;	
	
	Substitute(){
	}
	
	Substitute(String variable, String constant, KBRule rule){
		this.variable = variable;
		this.constant = constant;
		this.rule = rule;
	}
}

class ResolutionAlgo {
	
	boolean outputFlag = false;
	boolean oflag = false;	
	int count = 0;
	int limiter;
	
	
	public KBRule replace(int charToReplace, String variable, String constant ,KBRule ruleFromKB){		
		KBRule rule = ruleFromKB;
		int i = 0;
		while(i<rule.predicateArray.length){
			int j = 0;
			while(j<rule.predicateArray[i].arguments.length){
				if(!rule.predicateArray[i].arguments[j].equals(variable)){					
				}
				else{
					rule.predicateArray[i].arguments[j]=constant;
				}
				j++;
			}
			i++;
		}		
		return rule;		
	}
	
	public KBRule chooseRule(String np, HashMap<String, ArrayList<String>> KB, int selectedRule,int sentNumber){
		
		String predicate = null;
		KBRule rule = null;
		ArrayList<String> predList;
		String first = "";
		String second = "";
		String str = "";		
		
		if(!(np.charAt(0)=='~')){
			predicate='~'+np;
		}
		else{			
			predicate =np.substring(1);
		}
		
		if(!KB.containsKey(predicate)){			
		}
		else{
			predList = KB.get(predicate);
			int iterNumber = selectedRule + 1;
			while(iterNumber < predList.size()){
				KBRule s = new KBRule(predList.get(iterNumber));
				str = s.splitRule;			
				if(str.contains(predicate) && np.charAt(0) == '~' && !str.contains(np)){				   
					    rule = s;
						rule.selectedRule = iterNumber;
						break; 
				}
				else if(str.contains(predicate) && np.charAt(0) == '~'&& str.contains(np)){
					int i = 0;
					while(i<s.predicateArray.length){				  
					    	if(!s.predicateArray[i].predicateName.equals(predicate)){
					    		
					    	}
					    	else{
					    		int j = 0;
					    		while(j<s.predicateArray[i].arguments.length){
					    			first = first + s.predicateArray[i].arguments[j];
					    			j++;
					    		}
					    	}
					    	i++;
					}
					
					int k = 0;
					while(k<s.predicateArray.length){				    
					    	if(s.predicateArray[k].predicateName.equals(np)){
					    		int j = 0;
					    		while(j<s.predicateArray[k].arguments.length){
					    			second = second+s.predicateArray[k].arguments[j];
					    			j++;
					    		}
					    	}	
					    	k++;
					}			
					    
					if(!first.equals(second)){
						 rule = s;
						 rule.selectedRule = iterNumber;
						 break;	
					}
				}					   
				else if(str.contains(predicate)&& np.charAt(0)!='~'){					   
						rule = s;
						rule.selectedRule = iterNumber;
						break;
				}
				iterNumber++;	
			 }
		}
		return rule;
	}
	
	
	public ArrayList<KBRule> matchingVar(String query, String argument[], String pname ,char matchVar, KBRule rule){	
		
		KBRule given = new KBRule(query);
		String givenArg[] = {};
		boolean flag = false;
		boolean flag1 = false;
		KBRule r = null;
		ArrayList <Substitute> arrList = new ArrayList<Substitute>();
		int i = 0;
		while(i<given.predicateArray.length){		
			if(pname.contains(given.predicateArray[i].predicateName) || (given.predicateArray[i].predicateName).contains(pname)){		
				 givenArg = given.predicateArray[i].arguments;
				 break;				
			}
			i++;
		}
		int k = 0;
		while(k<argument.length){
			for(int l=0;l<givenArg.length;l++){
				if(!argument[k].equals(givenArg[l])){			
					
				}
				else{
					if(k!=l){
						flag1=true;
					}
				}
			}
			k++;
		}
		int q = 0;
		while(q<argument.length){
			if(flag1){
				break;
			}
			else{	
				int argLength = argument[q].length();
				int givenArgLength = givenArg[q].length();
				char argZero = argument[q].charAt(0);
				char givenArgZero = givenArg[q].charAt(0);
				int givenLength = givenArg.length;
				int argumentLength = argument.length;
				if(argLength == 1 && givenArgLength == 1){
					if(Character.isLowerCase(argZero) && Character.isLowerCase(givenArgZero)){					
						argument[q] = givenArg[q];
					}
				}			
				else if(argLength == 1){
					char ch = argZero;
					if(Character.isLowerCase(ch)){					
						char ga = givenArgZero;
						if(givenLength >=1 && Character.isUpperCase(ga)){								
							argument[q] = givenArg[q];
							arrList.add(new Substitute(String.valueOf(ch) , givenArg[q], rule));
							flag=true;
						}
					}
				}			
				else if(givenArgLength == 1){				
					char ch = givenArgZero;
					if(Character.isLowerCase(ch)){					
						char ga = argZero;
						if(argumentLength >=1 && Character.isUpperCase(ga)){						
							givenArg[q] = argument[q];
							arrList.add(new Substitute(String.valueOf(ch),argument[q],given));
							flag=true;
						}
					}
				}			
				else if(argLength == 1){				
					char ch = argZero;
					if(Character.isLowerCase(ch)){					
						char ga = givenArgZero;
						if(givenLength ==1 && Character.isLowerCase(ga))
						{
							argument[q] = givenArg[q];
							arrList.add(new Substitute(String.valueOf(ch), givenArg[q] ,rule));
							flag=true;
						}
					}
				}		
				else if(givenArg[q].equals(argument[q])){				
					arrList.add(new Substitute(argument[q],givenArg[q],rule));
					flag=true;
				}			
				else{				
					flag=false;
					break;
				}
								
			}
			q++;
		}
		
		ArrayList<KBRule> rep = new ArrayList<KBRule>();
		
	 	if(!flag==true){	 		
	 	}
	 	else{
	 		for(Substitute re : arrList){
	 			String variable = re.variable;
	 			String constant = re.constant;
	 		    r = re.rule;
	 		    rep.add(replace('0',variable,constant,r));
	 		}
	 	}
	 	return rep;		
	}
	
	public void resolution(ArrayList<String> sentences, String queryGiven) throws InterruptedException{		
		
		HashMap<String, ArrayList<String>> kb = new HashMap<String, ArrayList<String>>();
		StringBuilder sb = new StringBuilder();
		
		if(!(queryGiven.charAt(0)=='~') && Character.isAlphabetic(queryGiven.charAt(0))){
			sb.append("~");
			sb.append(queryGiven);
		}
		else{
			int openCount = 0;
			int closeCount = 0;
			if(queryGiven.charAt(0)=='~' ){
				for(int i=1;i<queryGiven.length();i++){
					if(queryGiven.charAt(i)=='(')
						openCount++;
					if(queryGiven.charAt(i)==')')
						closeCount++;
					sb.append(queryGiven.charAt(i));

					if(!Character.isAlphabetic(queryGiven.charAt(i))){
						if(openCount == closeCount)
							break;
					}														
				}
			}
		}
		
		String query = sb.toString();	
		
		String [] statement = sentences.toArray(new String[sentences.size()]);
		
		limiter = statement.length*15;	
		String trimRule = "";
		ArrayList<String> a;		
		int i = 0;
		
		while(i<statement.length){
			
			KBRule s = new KBRule(statement[i]);
			
			for(int r = 0; r < s.predicateArray.length; r++)
			{
				trimRule = s.predicateArray[r].predicateName;
				
				if(!kb.containsKey(s.predicateArray[r].predicateName))
				{
					a = new ArrayList<String>();
					a.add(statement[i].trim());
					kb.put(trimRule.trim(), a);
				}
				else
				{
					a = kb.get(s.predicateArray[r].predicateName);
					a.add(statement[i].trim());
					kb.put(trimRule.trim(), a);
				}
			}
			
			i++;
		}
		
		int j = 0;
		while(j<query.length()){
			if(query.charAt(j)=='(')
				break;
			j++;
		}
		
		KBRule rule = new KBRule(query);
		String []predicate=new String[rule.predicateArray.length];
		predicate[0]=query.substring(0, j); 
		unify(query,predicate,kb,true,-1,0,0);
		
		if(outputFlag){
		//	System.out.println("Hello world !!!");
			System.out.println("TRUE");
			outputFlag = false;
		}
		else{
		//	System.out.println("********Hello world !!!");
			System.out.println("FALSE");
		}
	}	
	
	public boolean unify(String query, String[] predicate, HashMap<String, ArrayList<String>> KB,boolean first, int choose,int co,int ruleNumber) throws InterruptedException{
		
		count = count + 1;
	
		if((count + 1) >(limiter + 1)){			
			return false;
		}
		
		boolean flag = false;
		boolean flag_rule = false;
		String nPredicate = "";
		int limit = -1;
		int taken = -1;				
		StringBuilder sb = new StringBuilder("");
		KBRule given = new KBRule(query);
		
		if(KB.containsKey(given.predicateArray[0].predicateName)){
			if(KB.get(given.predicateArray[0].predicateName).contains(query))
				return true;
		}
		
		int iterNo = 0;
		while(iterNo<predicate.length){	
			
			nPredicate = predicate[iterNo];
			String pp= null;
			
			if(!(nPredicate.charAt(0)=='~')){
				pp='~'+nPredicate;
			}
			else{
				pp =nPredicate.substring(1);
			}
			
			if(!KB.containsKey(pp)){
				
			}
			else{
				limit = (KB.get(pp)).size();
			}
			
			String argument[];
			String pname="";
			KBRule chosenRule;
			ArrayList<KBRule> la;
			chosenRule = chooseRule(nPredicate, KB, choose,0);
			
			if (chosenRule != null && ruleNumber == 0){
				taken = chosenRule.selectedRule;
				int i = 0;
				while(i<chosenRule.predicateArray.length){
					pname = chosenRule.predicateArray[i].predicateName.trim();    			 
			    
					if(( ruleNumber == 0 && predicate[iterNo].charAt(0)== '~' && !predicate[iterNo].equals(pname) && predicate[iterNo].contains(pname)) || (predicate[iterNo].charAt(0)!='~' && !predicate[iterNo].equals(pname) && 	predicate[iterNo].contains(pname.substring(1)))){
				    			    			    	
				       	argument = chosenRule.predicateArray[i].arguments;
				       	la = matchingVar(query, argument, pname ,'0',chosenRule);
				    	
				    	boolean flag1 = false;
				    	boolean flag2 = false;
				    	
				    	if(la.size() != 0){
				    		
				    		flag_rule =true;
				    		int w = 0;
				    		while(w < la.size()){				    		
				    			KBRule obj = la.get(w);
				    			if(ruleNumber == 0 && obj.splitRule==given.splitRule && !flag1){				    			  
				    				flag1=true;
				    				int z = 0;
				    				while(z<obj.predicateArray.length){			    				
				    					if(sb.toString().length()!=0)
				    						sb.append("|");
				    					sb.append(obj.predicateArray[z].predicateName+"(");
				    					int p = 0;
				    					while(p < obj.predicateArray[z].arguments.length){				    					
				    						if(ruleNumber == 0 && p!=0)
				    							sb.append(","+obj.predicateArray[z].arguments[p]);
				    						else
				    							sb.append(obj.predicateArray[z].arguments[p]);
				    						p++;
				    					}
				    					sb.append(")");
				    					z++;
				    				}
				    			}				    			
				    			else if(ruleNumber == 0 && obj.splitRule==chosenRule.splitRule && !flag2){				    			
				    				flag2=true;
				    				if(sb.length()!=0)
				    					sb.append("|");		
				    				int z = 0;
				    				while(z<obj.predicateArray.length){				    				
				    					if(z!=0)
				    						sb.append("|");
				    					sb.append(obj.predicateArray[z].predicateName+"(");
				    					int p = 0;
				    					while(p<obj.predicateArray[z].arguments.length){			    					
				    						if(p!=0)
				    							sb.append(","+obj.predicateArray[z].arguments[p]);
				    						else
				    							sb.append(obj.predicateArray[z].arguments[p]);
				    						p++;
				    					}
				    					sb.append(")");
				    					z++;
				    				}
				    			}	
				    			w++;
				    		}
				    		
				    		if(ruleNumber == 0 && flag2==false){
				    			if(sb.length()!=0)
			    					sb.append("|");
			    				sb.append(chosenRule.splitRule);
			    				break;
			    			}
			    			if(ruleNumber == 0 && flag1==false){
			    				if(sb.length()!=0)
			    					sb.append("|");
			    				sb.append(given.splitRule);
			    				break;
			    			}
				    	}				    	
				    	else{
				    		taken=-1;
				    	}
				    }
					i++;
				}
					
			}
			else {
					flag_rule=false;
			}				
			if(ruleNumber == 0 && flag_rule == true){
					break;
			}
			iterNo++;
		}			
		
		if(ruleNumber == 0 && flag_rule==true){
			
			KBRule ru = new KBRule(sb.toString());
			String[] pred=new String[ru.predicateArray.length];
			int v = 0;
			while(v < ru.predicateArray.length){
				pred[v]=ru.predicateArray[v].predicateName;
				v++;
			}
			
			StringBuilder sb1=new StringBuilder("");
			boolean answer[]=new boolean[pred.length];
			
			int h = 0;
			while(h<pred.length){
				answer[h]=true;
				h++;
			}
			
			int count_arg=0;
			
			comeHere:for(int ty=0;ty<pred.length-1;ty++){
				int wc = ty + 1;
				while(wc < pred.length){
					count_arg=0;	
				
					if((pred[ty].charAt(0)=='~'&& pred[ty].substring(1).equals(pred[wc])) 
							|| (pred[ty].charAt(0)!='~' && pred[wc].charAt(0)=='~' && pred[ty].equals(pred[wc].substring(1)))){
							int ct = 0;
							while(ct < ru.predicateArray[ty].arguments.length){						
								if(ru.predicateArray[ty].arguments[ct].equals(ru.predicateArray[wc].arguments[ct])){
									count_arg=count_arg+1;
								}
								ct++;
							}
					
						if(count_arg==ru.predicateArray[ty].arguments.length){
							answer[ty]=false;
							answer[wc]=false;
							break comeHere;
						}
					}	
					wc++;
				}							
			}
			int y = 0;
			while(y < answer.length){
			  if(answer[y]==true){
				if(ruleNumber == 0 && sb1.length()!=0)
					sb1.append("|");
				sb1.append(ru.predicateArray[y].predicateName+"(");
				int g = 0;
				while(g<ru.predicateArray[y].arguments.length){
					if(g!=0)
						sb1.append(",");
					sb1.append(ru.predicateArray[y].arguments[g]);
					g++;
				}
				sb1.append(")");			
			  }		
			  y++;
			}		

			if(!sb1.toString().equals("")){
				KBRule rule = new KBRule(sb1.toString());
				String[] predi=new String[rule.predicateArray.length];
				for(int vi=0;vi<rule.predicateArray.length;vi++){
					predi[vi]=rule.predicateArray[vi].predicateName;
				}
				unify(sb1.toString(),predi,KB,false,-1,co,0);
			}
			else{
				flag=true;
				oflag=true;
				outputFlag = true;
				return flag;
			}
		}
		
		else{
			if(ruleNumber == 0 && taken == -1){
				flag=false;
				return flag;
			}
			else if(ruleNumber == 0 && taken == limit-1){
				flag=false;
				return flag;
			}
			else if(ruleNumber == 0 && taken < limit){
				unify(query,predicate,KB,false,taken,co,0);		
			}
		}
		if(ruleNumber == 0 && flag==false && co <= limit && oflag==false){
			if(ruleNumber == 0 && count < limiter){
				unify(query,predicate,KB,false,taken,co+1,0);
			}
			else{
				return false;
			}
		}
		return flag;
	}
	
}

class CNFConversion{		
	
	//Methods for solving Implication
	String negation(String str){
		int len = str.length();
		int flag;
		StringBuilder sb = new StringBuilder();
		for(int i=0;i<len;i++){
			flag = 0;
			char ch = str.charAt(i);
			if(Character.isUpperCase(ch)){
				for(int j=i;j<len;j++){
					if(str.charAt(j)==')'){
						flag = 1;
						break;
					}
					if(str.charAt(j)=='('){
						flag = 0;
						break;
					}
				}
				if(flag == 0){
					if(i>0){
						char prevChar = str.charAt(i-1);
						if(prevChar=='~'){
							sb.append(ch);	
						}
						else{
							sb.append('~');
							sb.append(ch);	
						}
					}
					else{
						sb.append('~');
						sb.append(ch);
					}
				}
				else{
					sb.append(ch);
				}
							
			}
			else{
				if(ch == '|'){
					sb.append('&');
				}
				else if(ch == '&'){
					sb.append('|');
				}
				else if(ch == '~'){
				}
				else{
					sb.append(ch);
				}
			}
		}
		return sb.toString();
	}
	
	String simplifyExpression(String str){
		if(str.contains("=>")){
			StringBuilder lhs = new StringBuilder();
			StringBuilder rhs = new StringBuilder();
			StringBuilder newString = new StringBuilder();
			for(int i=0;i<str.length();i++){
				if(str.charAt(i)!='='){
					lhs.append(str.charAt(i));
				}
				else{
					i+=2;
					String afterNegation = negation(lhs.toString());
					newString.append(afterNegation);
					newString.append("|");
					for(int j=i;j<str.length();j++){
						rhs.append(str.charAt(j));
					}
					newString.append(rhs.toString());
					return newString.toString();
				}
			}
		}
		return str;
	}
	
	String checkImplication(String str){
		if(!str.contains("=>")){
			return str;
		}
		StringBuilder sb = new StringBuilder();
		StringBuilder st = new StringBuilder();
		sb.append('(');		
		int len =  str.length();
		int i = 1;
		while(i<len){
				char ch = str.charAt(i);
				if(ch == '('){	
					if(!(Character.isAlphabetic(str.charAt(i-1))&& Character.isAlphabetic(str.charAt(i+1)))){
						int openCount = 0;
						int closeCount = 0;
						for(int j=i;j<str.length();j++){
							i++;
							if(str.charAt(j)=='(')
								openCount++;
							if(str.charAt(j)==')')
								closeCount++;
							st.append(str.charAt(j));
							if(str.charAt(j)==')'){
								if(openCount == closeCount)
									break;
							}
						}
						
						String simplified = checkImplication(st.toString());
						sb.append(simplified);
						st = new StringBuilder();
						
					}
					else{
						sb.append(ch);
						i++;
					}
				}
				else{
					if(ch != ')'){
						sb.append(ch);
						i++;
					}
					else{
						sb.append(ch);
						if(!Character.isLowerCase(str.charAt(i-1))){
							CNFConversion cnf = new CNFConversion();
							String se = cnf.simplifyExpression(sb.toString());
							return se;
						}
						i++;
					}
				}
		}
		return sb.toString();
	}
	
	//Methods for taking the negation inwards
	String removeNegation(String str,char prevChar){
		StringBuilder sb = new StringBuilder();
		StringBuilder st = new StringBuilder();
		sb.append('(');		
		int len =  str.length();
		int i = 1;
		char prev = prevChar;
		while(i<len){
				char ch = str.charAt(i);				
				if(ch == '('){	
					if(!Character.isAlphabetic(str.charAt(i-1))){
							prev = str.charAt(i-1);
					}
					if(!(Character.isAlphabetic(str.charAt(i-1))&& Character.isAlphabetic(str.charAt(i+1)))){	
						int openCount = 0;
						int closeCount = 0;
						for(int j=i;j<str.length();j++){
							i++;
							if(str.charAt(j)=='(')
								openCount++;
							if(str.charAt(j)==')')
								closeCount++;
							st.append(str.charAt(j));
							if(str.charAt(j)==')'){
								if(openCount == closeCount)
									break;
							}
						}
						String simplified = removeNegation(st.toString(),prev);
						sb.append(simplified);
						st = new StringBuilder();
		
					}
					else{
						sb.append(ch);
						i++;
					}
				}
				else{
					if(ch != ')' && ch != '~' ){
						sb.append(ch);
						i++;
					}
					else if(ch == '~'){
						if(Character.isAlphabetic(str.charAt(i+1))){
							sb.append(ch);
							i++;
						}
						else{
							i++;
						}
					}
					else{
						sb.append(ch);
						if(!Character.isLowerCase(str.charAt(i-1))){
							CNFConversion cnf = new CNFConversion();
							prev = prevChar;
							String se = cnf.negateExpression(sb.toString(),prev);
							prev = prevChar;
							return se;
						}
						i++;
					}
				}
		}
		return sb.toString();
	}
	
	String negateExpression(String str,char prevChar){
			StringBuilder sb = new StringBuilder();
			if(prevChar != '~'){
				return str;
			}
			for(int i=0;i<str.length();i++){
				if(str.charAt(i)=='&'){
					sb.append('|');
				}
				else if(str.charAt(i)=='|'){
					sb.append('&');
				}
				else if(str.charAt(i)=='~'){
					
				}
				else{
					int flag = 0;
					if(Character.isUpperCase(str.charAt(i)) && !(str.charAt(i-1)=='~')){
						for(int j=i;j<str.length();j++){
							if(str.charAt(j)==')'){
								flag = 1;
								break;
							}
							if(str.charAt(j)=='('){
								flag = 0;
								break;
							}
						}
						if(flag==0){
							sb.append('~');
							sb.append(str.charAt(i));
						}
					}
					else if(Character.isUpperCase(str.charAt(i)) && (str.charAt(i-1)=='~')){
						for(int j=i;j<str.length();j++){
							if(str.charAt(j)==')'){
								flag = 1;
								break;
							}
							if(str.charAt(j)=='('){
								flag = 0;
								break;
							}
						}
						if(flag==0){
							sb.append(str.charAt(i));
						}
					}
					else{
						sb.append(str.charAt(i));
					}
					
				}
			}
			return sb.toString();
	}
	
	//Method for distribution
	String distributionStep(String str,ArrayList<String> storeSent){
		StringBuilder sb = new StringBuilder();
		StringBuilder st = new StringBuilder();
		sb.append('(');		
		int len =  str.length();
		int i = 1;
		while(i<len){
				char ch = str.charAt(i);
				if(ch == '('){	
					if(!Character.isAlphabetic(str.charAt(i-1))){
							int openCount = 0;
							int closeCount = 0;
							for(int j=i;j<str.length();j++){
								i++;
								if(str.charAt(j)=='(')
									openCount++;
								if(str.charAt(j)==')')
									closeCount++;
								st.append(str.charAt(j));
								if(str.charAt(j)==')'){
									if(openCount == closeCount)
										break;
								}
							}
							String simplified = distributionStep(st.toString(),storeSent);
							sb.append(simplified);
							st = new StringBuilder();						
					}
					else{
						sb.append(ch);
						i++;
					}
				}
				else{
					if(ch != ')'){
						sb.append(ch);
						i++;
					}
					else{
						sb.append(ch);
						if(!Character.isAlphabetic(str.charAt(i-1))){
							CNFConversion db = new CNFConversion();
							String se = db.simplifyDistributionExpression(sb.toString(),storeSent);
							return se;
						}
						i++;
					}
				}
		}
		return sb.toString();
	}
	
	String simplifyDistributionExpression(String str,ArrayList<String> storeSent){
		String k = "";
		k = str;
		if(!storeSent.isEmpty()){
			int lastSentIndex = storeSent.size() - 1;
			
			if(lastSentIndex >= 0){
				String exist = storeSent.get(lastSentIndex);
				int lastStringLen = exist.length();
				int lastIndex = str.lastIndexOf(exist);
				if(lastIndex > 1){
				
					if(lastIndex != 1){
						int breakIndex = lastIndex -1 ;
						String lhs = str.substring(0, breakIndex);
						String rhs = str.substring(breakIndex+1,str.length());
						if(str.charAt(breakIndex)=='|'){
							k = orOverAndChg(lhs,rhs);
						}
					}
					else{
						int breakIndex = lastIndex + lastStringLen ;
						String lhs = str.substring(0, breakIndex);
						String rhs = str.substring(breakIndex+1,str.length());
						if(str.charAt(breakIndex)=='|'){
							k = orOverAndSimp(lhs,rhs);
						}
					}
				}
			}
		}
		storeSent.add(str);
	
		return k;
	}
	
	String orOverAndChg(String lhs,String rhs){
		
		StringBuilder sb = new StringBuilder();
		sb.append('(');
		if(rhs.contains("&")){
			String[] parts = rhs.split("&");
			for(int i=0;i<parts.length;i++){				
				sb.append('(');
				sb.append(lhs);				
				sb.append("|");
				sb.append(parts[i]);
				sb.append(')');
				if(i<=parts.length-2){
					sb.append('&');
				}
			}
		}
		else{
			return (lhs+"|"+rhs);
		}
		
		sb.append(')');
		return sb.toString();
	}
	
	String orOverAndSimp(String lhs,String rhs){
		StringBuilder sb = new StringBuilder();
		sb.append('(');
		if(lhs.contains("&")){
			String[] parts = lhs.split("&");
			for(int i=0;i<parts.length;i++){
				sb.append('(');
				sb.append(parts[i]);
				sb.append("|");
				sb.append(rhs);
				sb.append(')');
				if(i<=parts.length-2){
					sb.append('&');
				}
			}
		}
		else{
			return (lhs+"|"+rhs);
		}
		sb.append(')');
		return sb.toString();		
	}
	
	String orOverAnd(String str){
		StringBuilder sb = new StringBuilder();
		int ind = str.indexOf('|');
		String[] parts = str.split("|",ind);
		String lhs = parts[0];
		StringBuilder st = new StringBuilder();
		for(int i=1;i<lhs.length();i++){
			if(lhs.charAt(i)=='(' && Character.isAlphabetic(lhs.charAt(i+1))){
				st.append(lhs.charAt(i));
			}
			else{
				st.append(lhs.charAt(i));
			}
		}
		lhs = st.toString();
		String rhs = parts[1];
		StringBuilder sr = new StringBuilder();
		for(int i=1;i<rhs.length();i++){
			if(rhs.charAt(i)=='('){	
				if(Character.isAlphabetic(rhs.charAt(i+1)))
					sr.append(rhs.charAt(i));
			}
			else if(rhs.charAt(i)==')'){
				if(Character.isAlphabetic(rhs.charAt(i-1)))
					sr.append(rhs.charAt(i));
			}
			else{
				sr.append(rhs.charAt(i));
			}
		}
		rhs = sr.toString();
		String[] rhsParts = rhs.split("&");
		sb.append("(");
		for(int i=0;i<rhsParts.length;i++){
				sb.append("(");
				sb.append(lhs);
				sb.append("|");
				sb.append(rhsParts[i]);
				sb.append(")");
				if(i<=rhsParts.length-2){
					sb.append("&");
				}
		}
		sb.append(")");
		return sb.toString();
	}
	
	String andOverOr(String str){
		return str;
	}
	
}
class ResAlgo {
	public static void main(String[] args) throws IOException, InterruptedException{
		BufferedReader br;
		int count = 1;
		String rule;
		ArrayList<String> storeSent = new ArrayList<String>();
		ArrayList<String> queryList = new ArrayList<String>();
    	ArrayList<String> sentences = new ArrayList<String>();
		try {
			br = new BufferedReader(new FileReader("src/input.txt"));
			PrintStream out = new PrintStream(new FileOutputStream("src/output.txt"));
			System.setOut(out);
			StringBuilder sb = new StringBuilder();
			String line = br.readLine();
			while (line != null) {
		        sb.append(line);
		        sb.append(System.lineSeparator());
		        line = br.readLine();
		    }			
		    String everything = sb.toString();
		    String lines[] = everything.split("\\r?\\n");
		    String l = lines[0];
		    int n = Integer.parseInt(l);
		    for(int i=0;i<n;i++){
		    	queryList.add(lines[1+i]);
		    	count++;
		    }
		    String a = lines[count];
		    int k = Integer.parseInt(a);
		    ResolutionAlgo inf = new ResolutionAlgo();
		    inf.limiter = 15 * k;
		    
		    for(int j=0;j<k;j++){
		    	rule = lines[count+1+j];
		    	String str = rule.replaceAll("\\s","");
		    	if(str.charAt(0)!='('){
		    		StringBuilder strBld = new StringBuilder();
		    		strBld.append('(');
		    		for(int q=0;q<str.length();q++){
		    			strBld.append(str.charAt(q));
		    		}
		    		strBld.append(')');
		    		str = strBld.toString();
		    	}
		    	CNFConversion cnf = new CNFConversion();	
		    	String implicationRemovedString = cnf.checkImplication(str);
		    	String negatedString = cnf.removeNegation(implicationRemovedString,'0');
		    	String s;
		    	if(negatedString.contains("&") && negatedString.contains("|"))
		    		 s = cnf.distributionStep(negatedString,storeSent);
		    	else
		    		 s = negatedString;
		    	storeSent.clear();
		    	String[] splitOnAnd = s.split("&");
		    	for(int i=0;i<splitOnAnd.length;i++){
					String sp = splitOnAnd[i];
					StringBuilder st = new StringBuilder();
					if(sp.charAt(0)=='~' || Character.isAlphabetic(sp.charAt(0))){
						st.append(sp.charAt(0));
					}
					for(int t=1;t<sp.length();t++){	
						if(sp.charAt(t)=='('){
							if(Character.isAlphabetic(sp.charAt(t-1)) && Character.isAlphabetic(sp.charAt(t+1))){
								st.append(sp.charAt(t));
							}
						}
						else if(sp.charAt(t)==')'){
							if(Character.isAlphabetic(sp.charAt(t-1)) ){
								st.append(sp.charAt(t));
							}
						}
						else{
							st.append(sp.charAt(t));
						}
					}
					sentences.add(st.toString());
				}		
		    }
		    for(int j=0;j<queryList.size();j++){
		    	String query = queryList.get(j);
				ResolutionAlgo i = new ResolutionAlgo();
				i.resolution(sentences,query);
				count = 0;
		    }
		} 
		catch (FileNotFoundException e) {
				e.printStackTrace();
		} 		
	}
}